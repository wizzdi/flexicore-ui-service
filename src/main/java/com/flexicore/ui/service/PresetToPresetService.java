package com.flexicore.ui.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.PresetToPresetRepository;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToPreset;
import com.flexicore.ui.model.Preset_;
import com.flexicore.ui.request.PresetToPresetCreate;
import com.flexicore.ui.request.PresetToPresetFiltering;
import com.flexicore.ui.request.PresetToPresetUpdate;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
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
public class PresetToPresetService implements Plugin {

	@Autowired
	private BasicService basicService;

	
	@Autowired
	private PresetToPresetRepository presetToPresetRepository;


	public PaginationResponse<PresetToPreset> getAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
																	SecurityContextBase securityContext) {
		List<PresetToPreset> list = listAllPresetToPresets(presetToPresetFiltering, securityContext);
		long count = presetToPresetRepository.countAllPresetToPresets(presetToPresetFiltering,
				securityContext);
		return new PaginationResponse<>(list, presetToPresetFiltering, count);
	}

	public List<PresetToPreset> listAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
													   SecurityContextBase securityContext) {
		return presetToPresetRepository.listAllPresetToPresets(presetToPresetFiltering, securityContext);
	}

	public boolean updatePresetToPresetNoMerge(PresetToPresetCreate createPreset, PresetToPreset preset) {
		boolean update = basicService.updateBasicNoMerge(createPreset, preset);

		if (createPreset.getParentPreset() != null && (preset.getParentPreset() == null || !createPreset.getParentPreset().getId().equals(preset.getParentPreset().getId()))) {
			preset.setParentPreset(createPreset.getParentPreset());
			update = true;
		}

		if (createPreset.getChildPreset() != null && (preset.getChildPreset() == null || !createPreset.getChildPreset().getId().equals(preset.getChildPreset().getId()))) {
			preset.setChildPreset(createPreset.getChildPreset());
			update = true;
		}

		if (createPreset.getChildPath() != null && !createPreset.getChildPath().equals(preset.getChildPath())) {
			preset.setChildPath(createPreset.getChildPath());
			update = true;
		}

		if (createPreset.getParentPath() != null && !createPreset.getParentPath().equals(preset.getParentPath())) {
			preset.setParentPath(createPreset.getParentPath());
			update = true;
		}

		return update;
	}

	public PresetToPreset updatePresetToPreset(PresetToPresetUpdate updatePreset,
											   SecurityContextBase securityContext) {
		if (updatePresetToPresetNoMerge(updatePreset, updatePreset.getPresetToPreset())) {
			presetToPresetRepository.merge(updatePreset.getPresetToPreset());
		}
		return updatePreset.getPresetToPreset();

	}

	public PresetToPreset createPresetToPreset(PresetToPresetCreate createPreset,
											   SecurityContextBase securityContext) {
		PresetToPreset preset = createPresetToPresetNoMerge(createPreset, securityContext);
		presetToPresetRepository.merge(preset);
		return preset;

	}

	public PresetToPreset createPresetToPresetNoMerge(PresetToPresetCreate createPreset,
													  SecurityContextBase securityContext) {
		PresetToPreset preset = new PresetToPreset();
		preset.setId(UUID.randomUUID().toString());
		updatePresetToPresetNoMerge(createPreset, preset);
		BaseclassService.createSecurityObjectNoMerge(preset,securityContext);
		return preset;
	}


	public void validate(PresetToPresetFiltering presetToPresetFiltering, SecurityContextBase securityContext) {
		Set<String> parentPresetIds=presetToPresetFiltering.getParentPrestIds();
		Map<String,Preset> parentPresets=parentPresetIds.isEmpty()?new HashMap<>():presetToPresetRepository.listByIds(Preset.class,parentPresetIds,Preset_.security,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		parentPresetIds.removeAll(parentPresets.keySet());
		if(!parentPresetIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Presets with ids "+parentPresetIds);
		}
		presetToPresetFiltering.setParentPresets(new ArrayList<>(parentPresets.values()));

		Set<String> childPresetIds=presetToPresetFiltering.getChildPresetIds();
		Map<String,Preset> childPresets=childPresetIds.isEmpty()?new HashMap<>():presetToPresetRepository.listByIds(Preset.class,childPresetIds,Preset_.security,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		childPresetIds.removeAll(childPresets.keySet());
		if(!childPresetIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Presets with ids "+childPresetIds);
		}
		presetToPresetFiltering.setChildPresets(new ArrayList<>(childPresets.values()));
	}


	public void validate(PresetToPresetCreate createPresetToPreset, SecurityContextBase securityContext) {
		String parentPresetId = createPresetToPreset.getParentPresetId();
		Preset parentPreset = parentPresetId == null ? null : getByIdOrNull(parentPresetId, Preset.class, Preset_.security, securityContext);
		if (parentPreset == null && parentPresetId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Preset with id " + parentPresetId);
		}
		createPresetToPreset.setParentPreset(parentPreset);

		String childPresetId = createPresetToPreset.getChildPresetId();
		Preset childPreset = childPresetId == null ? null : getByIdOrNull(childPresetId, Preset.class, Preset_.security, securityContext);
		if (childPreset == null && childPresetId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Preset with id " + childPresetId);
		}
		createPresetToPreset.setChildPreset(childPreset);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return presetToPresetRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return presetToPresetRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return presetToPresetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return presetToPresetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return presetToPresetRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return presetToPresetRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return presetToPresetRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		presetToPresetRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		presetToPresetRepository.massMerge(toMerge);
	}
}
