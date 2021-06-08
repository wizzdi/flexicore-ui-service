package com.flexicore.ui.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.PresetToEntityRepository;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.PreferedPresetRequest;
import com.flexicore.ui.request.PresetToEntityCreate;
import com.flexicore.ui.request.PresetToEntityFiltering;
import com.flexicore.ui.request.PresetToEntityUpdate;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import com.wizzdi.flexicore.security.service.RoleToUserService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;


@Extension
@Component
public class PresetToEntityService implements Plugin {

	private static final Map<String, Integer> orderMap;

	static {
		orderMap = new HashMap<>();
		orderMap.put(PresetToUser.class.getCanonicalName(), 0);
		orderMap.put(PresetToRole.class.getCanonicalName(), 1);
		orderMap.put(PresetToTenant.class.getCanonicalName(), 2);

	}

	private static final Comparator<PresetToEntity> PRESET_LINK_COMPARATOR = Comparator
			.comparing(f -> orderMap.getOrDefault(f.getClass()
					.getCanonicalName(), Integer.MAX_VALUE));
	private static final Logger logger=LoggerFactory.getLogger(PresetToEntityService.class);

	
	@Autowired
	private PresetToEntityRepository presetToEntityRepository;

	@Autowired
	private BasicService basicService;

	@Autowired
	private RoleToUserService roleToUserService;

	public PresetToEntity presetToEntityUpdate(
			PresetToEntityUpdate presetToEntityUpdate,
			SecurityContextBase securityContext) {
		if (presetToEntityUpdateNoMerge(presetToEntityUpdate, presetToEntityUpdate.getPresetToEntity())) {
			presetToEntityRepository.merge(presetToEntityUpdate.getPresetToEntity());
		}
		return presetToEntityUpdate.getPresetToEntity();
	}

	public boolean presetToEntityUpdateNoMerge(
			PresetToEntityCreate presetToEntityCreate,
			PresetToEntity presetToEntity) {
		boolean update = basicService.updateBasicNoMerge(presetToEntityCreate, presetToEntity);
		if (presetToEntityCreate.getPriority() != null && presetToEntityCreate.getPriority() != presetToEntity.getPriority()) {
			presetToEntity.setPriority(presetToEntityCreate.getPriority());
			update = true;
		}
		if (presetToEntityCreate.getEnabled() != null && presetToEntityCreate.getEnabled() != presetToEntity.isEnabled()) {
			presetToEntity.setEnabled(presetToEntityCreate.getEnabled());
			update = true;
		}

		if (presetToEntityCreate.getPreset() != null && (presetToEntity.getPreset() == null || !presetToEntityCreate.getPreset().getId().equals(presetToEntity.getPreset().getId()))) {
			presetToEntity.setPreset(presetToEntityCreate.getPreset());
			update = true;
		}

		return update;
	}

	public void validate(PresetToEntityCreate presetToEntityCreate,
			SecurityContextBase securityContext) {
		String presetId = presetToEntityCreate.getPresetId();
		Preset preset = presetId != null ? getByIdOrNull(presetId, Preset.class, Preset_.security, securityContext) : null;
		if (preset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no preset with id " + presetId);
		}
		presetToEntityCreate.setPreset(preset);
	}

	public PaginationResponse<PresetToEntity> getAllPresetToEntities(
			PresetToEntityFiltering presetToEntityFiltering,
			SecurityContextBase securityContext) {
		List<PresetToEntity> list = listAllPresetToEntities(presetToEntityFiltering, securityContext);
		long count = presetToEntityRepository.countAllPresetToEntities(presetToEntityFiltering, securityContext);
		return new PaginationResponse<>(list, presetToEntityFiltering, count);
	}

	public List<PresetToEntity> listAllPresetToEntities(PresetToEntityFiltering presetToEntityFiltering, SecurityContextBase securityContext) {
		return presetToEntityRepository.listAllPresetToEntities(presetToEntityFiltering, securityContext);
	}

	public PresetToEntity presetToEntityCreate(
			PresetToEntityCreate presetToEntityCreate,
			SecurityContextBase securityContext) {
		PresetToEntity presetToEntity = presetToEntityCreateNoMerge(presetToEntityCreate, securityContext);
		presetToEntityRepository.merge(presetToEntity);
		return presetToEntity;

	}

	private PresetToEntity presetToEntityCreateNoMerge(
			PresetToEntityCreate presetToEntityCreate,
			SecurityContextBase securityContext) {
		PresetToEntity presetToEntity = new PresetToEntity();
		presetToEntity.setId(UUID.randomUUID().toString());
		presetToEntityUpdateNoMerge(presetToEntityCreate, presetToEntity);
		BaseclassService.createSecurityObjectNoMerge(presetToEntity,securityContext);
		return presetToEntity;
	}

	public List<Preset> getPreferredPresets(PreferedPresetRequest preferedPresetRequest, SecurityContextBase securityContext) {
		Map<String, List<PresetToEntity>> map = new HashMap<>();
		PresetToEntityFiltering presetToEntityFiltering = new PresetToEntityFiltering();
		ArrayList<Baseclass> rightside = new ArrayList<>(securityContext.getTenants());
		rightside.add(securityContext.getUser());
		rightside.addAll(roleToUserService.listAllRoleToUsers(new RoleToUserFilter().setSecurityUsers(Collections.singletonList(securityContext.getUser())), null).stream().map(f->f.getLeftside()).collect(Collectors.toList()));
		presetToEntityFiltering.setEntities(rightside);
		List<PresetToEntity> links = presetToEntityRepository.listAllPresetToEntities(presetToEntityFiltering, securityContext).parallelStream().filter(f -> f.getPreset() != null).sorted(PRESET_LINK_COMPARATOR.thenComparing(PresetToEntity::getPriority)).collect(Collectors.toList());
		for (PresetToEntity presetToEntity : links) {
			String canonicalName = presetToEntity.getPreset().getClass().getCanonicalName();
			List<PresetToEntity> presetToEntities = map.computeIfAbsent(canonicalName, f -> new ArrayList<>());
			PresetToEntity last = presetToEntities.isEmpty() ? null : presetToEntities.get(0);
			if (last == null || (last.getClass().equals(presetToEntity.getClass()) && last.getPriority() == presetToEntity.getPriority())) {
				presetToEntities.add(presetToEntity);
			}

		}
		return map.values().parallelStream().flatMap(List::stream).map(f -> f.getPreset()).collect(Collectors.toList());

	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return presetToEntityRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return presetToEntityRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return presetToEntityRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return presetToEntityRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return presetToEntityRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return presetToEntityRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return presetToEntityRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		presetToEntityRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		presetToEntityRepository.massMerge(toMerge);
	}
}
