package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.data.jsoncontainers.SortingOrder;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.SortParameter;
import com.flexicore.request.RoleFilter;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.service.RoleService;
import com.flexicore.ui.data.PresetToEntityRepository;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.PreferedPresetRequest;
import com.flexicore.ui.request.PresetToEntityCreate;
import com.flexicore.ui.request.PresetToEntityFiltering;
import com.flexicore.ui.request.PresetToEntityUpdate;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class PresetToEntityService implements ServicePlugin {

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
	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private PresetToEntityRepository presetToEntityRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	@Autowired
	private RoleService roleService;

	public PresetToEntity presetToEntityUpdate(
			PresetToEntityUpdate presetToEntityUpdate,
			SecurityContext securityContext) {
		if (presetToEntityUpdateNoMerge(presetToEntityUpdate,
				presetToEntityUpdate.getPresetToEntity())) {
			presetToEntityRepository.merge(presetToEntityUpdate
					.getPresetToEntity());
		}
		return presetToEntityUpdate.getPresetToEntity();
	}

	public boolean presetToEntityUpdateNoMerge(
			PresetToEntityCreate presetToEntityCreate,
			PresetToEntity presetToEntity) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(
				presetToEntityCreate, presetToEntity);
		if (presetToEntityCreate.getPriority() != null
				&& presetToEntityCreate.getPriority() != presetToEntity
						.getPriority()) {
			presetToEntity.setPriority(presetToEntityCreate.getPriority());
			update = true;
		}
		if (presetToEntityCreate.getEnabled() != null
				&& presetToEntityCreate.getEnabled() != presetToEntity
						.isEnabled()) {
			presetToEntity.setEnabled(presetToEntityCreate.getEnabled());
			update = true;
		}

		if (presetToEntityCreate.getPreset() != null
				&& (presetToEntity.getLeftside() == null || !presetToEntityCreate
						.getPreset().getId()
						.equals(presetToEntity.getLeftside().getId()))) {
			presetToEntity.setLeftside(presetToEntityCreate.getPreset());
			update = true;
		}

		return update;
	}

	public void validate(PresetToEntityCreate presetToEntityCreate,
			SecurityContext securityContext) {
		String presetId = presetToEntityCreate.getPresetId();
		Preset preset = presetId != null ? getByIdOrNull(presetId,
				Preset.class, null, securityContext) : null;
		if (preset == null) {
			throw new BadRequestException("no preset with id " + presetId);
		}
		presetToEntityCreate.setPreset(preset);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return presetToEntityRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<PresetToEntity> getAllPresetToEntities(
			PresetToEntityFiltering presetToEntityFiltering,
			SecurityContext securityContext) {
		List<PresetToEntity> list = listAllPresetToEntities(
				presetToEntityFiltering, securityContext);
		long count = presetToEntityRepository.countAllPresetToEntities(
				presetToEntityFiltering, securityContext);
		return new PaginationResponse<>(list, presetToEntityFiltering, count);
	}

	public List<PresetToEntity> listAllPresetToEntities(
			PresetToEntityFiltering presetToEntityFiltering,
			SecurityContext securityContext) {
		return presetToEntityRepository.listAllPresetToEntities(
				presetToEntityFiltering, securityContext);
	}

	public PresetToEntity presetToEntityCreate(
			PresetToEntityCreate presetToEntityCreate,
			SecurityContext securityContext) {
		PresetToEntity presetToEntity = presetToEntityCreateNoMerge(
				presetToEntityCreate, securityContext);
		presetToEntityRepository.merge(presetToEntity);
		return presetToEntity;

	}

	private PresetToEntity presetToEntityCreateNoMerge(
			PresetToEntityCreate presetToEntityCreate,
			SecurityContext securityContext) {
		PresetToEntity presetToEntity = new PresetToEntity(
				presetToEntityCreate.getName(), securityContext);
		presetToEntityUpdateNoMerge(presetToEntityCreate, presetToEntity);
		return presetToEntity;
	}

	public List<Preset> getPreferredPresets(
			PreferedPresetRequest preferedPresetRequest,
			SecurityContext securityContext) {
		Map<String, List<PresetToEntity>> map = new HashMap<>();
		PresetToEntityFiltering presetToEntityFiltering = new PresetToEntityFiltering();
		presetToEntityFiltering.setSort(Arrays.asList(new SortParameter(
				"priority", SortingOrder.DESCENDING)));
		ArrayList<Baseclass> rightside = new ArrayList<>(
				securityContext.getTenants());
		rightside.add(securityContext.getUser());
		rightside.addAll(roleService.listAllRoles(
				new RoleFilter().setUsers(Collections
						.singletonList(securityContext.getUser())), null));
		presetToEntityFiltering.setRightside(rightside);
		List<PresetToEntity> links = presetToEntityRepository
				.listAllPresetToEntities(presetToEntityFiltering,
						securityContext)
				.parallelStream()
				.filter(f -> f.getLeftside() != null)
				.sorted(PRESET_LINK_COMPARATOR.thenComparing(f -> f
						.getPriority())).collect(Collectors.toList());
		for (PresetToEntity presetToEntity : links) {
			String canonicalName = presetToEntity.getLeftside().getClass()
					.getCanonicalName();
			List<PresetToEntity> presetToEntities = map.computeIfAbsent(
					canonicalName, f -> new ArrayList<>());
			PresetToEntity last = presetToEntities.isEmpty()
					? null
					: presetToEntities.get(0);
			if (last == null
					|| (last.getClass().equals(presetToEntity.getClass()) && last
							.getPriority() == presetToEntity.getPriority())) {
				presetToEntities.add(presetToEntity);
			}

		}
		return map.values().parallelStream().flatMap(List::stream)
				.map(f -> f.getLeftside()).collect(Collectors.toList());

	}

}
