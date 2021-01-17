package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.ui.data.PresetToPresetRepository;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToPreset;
import com.flexicore.ui.request.PresetToPresetCreate;
import com.flexicore.ui.request.PresetToPresetFiltering;
import com.flexicore.ui.request.PresetToPresetUpdate;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class PresetToPresetService implements ServicePlugin {

	@Autowired
	private BaseclassNewService baseclassNewService;

	@PluginInfo(version = 1)
	@Autowired
	private PresetToPresetRepository presetToPresetRepository;


	public PaginationResponse<PresetToPreset> getAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
																	SecurityContext securityContext) {
		List<PresetToPreset> list = listAllPresetToPresets(presetToPresetFiltering, securityContext);
		long count = presetToPresetRepository.countAllPresetToPresets(presetToPresetFiltering,
				securityContext);
		return new PaginationResponse<>(list, presetToPresetFiltering, count);
	}

	public List<PresetToPreset> listAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
													   SecurityContext securityContext) {
		return presetToPresetRepository.listAllPresetToPresets(presetToPresetFiltering, securityContext);
	}

	public boolean updatePresetToPresetNoMerge(PresetToPresetCreate createPreset, PresetToPreset preset) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(createPreset, preset);

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
											   SecurityContext securityContext) {
		if (updatePresetToPresetNoMerge(updatePreset, updatePreset.getPresetToPreset())) {
			presetToPresetRepository.merge(updatePreset.getPresetToPreset());
		}
		return updatePreset.getPresetToPreset();

	}

	public PresetToPreset createPresetToPreset(PresetToPresetCreate createPreset,
											   SecurityContext securityContext) {
		PresetToPreset preset = createPresetToPresetNoMerge(createPreset, securityContext);
		presetToPresetRepository.merge(preset);
		return preset;

	}

	public PresetToPreset createPresetToPresetNoMerge(PresetToPresetCreate createPreset,
													  SecurityContext securityContext) {
		PresetToPreset preset = new PresetToPreset(createPreset.getName(), securityContext);
		updatePresetToPresetNoMerge(createPreset, preset);
		return preset;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
												 List<String> batchString, SecurityContext securityContext) {
		return presetToPresetRepository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public void validate(PresetToPresetFiltering presetToPresetFiltering, SecurityContext securityContext) {
		Set<String> parentPresetIds=presetToPresetFiltering.getParentPrestIds();
		Map<String,Preset> parentPresets=parentPresetIds.isEmpty()?new HashMap<>():presetToPresetRepository.listByIds(Preset.class,parentPresetIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		parentPresetIds.removeAll(parentPresets.keySet());
		if(!parentPresetIds.isEmpty()){
			throw new BadRequestException("No Presets with ids "+parentPresetIds);
		}
		presetToPresetFiltering.setParentPresets(new ArrayList<>(parentPresets.values()));

		Set<String> childPresetIds=presetToPresetFiltering.getChildPresetIds();
		Map<String,Preset> childPresets=childPresetIds.isEmpty()?new HashMap<>():presetToPresetRepository.listByIds(Preset.class,childPresetIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		childPresetIds.removeAll(childPresets.keySet());
		if(!childPresetIds.isEmpty()){
			throw new BadRequestException("No Presets with ids "+childPresetIds);
		}
		presetToPresetFiltering.setChildPresets(new ArrayList<>(childPresets.values()));
	}


	public void validate(PresetToPresetCreate createPresetToPreset, SecurityContext securityContext) {
		String parentPresetId = createPresetToPreset.getParentPresetId();
		Preset parentPreset = parentPresetId == null ? null : getByIdOrNull(parentPresetId, Preset.class, null, securityContext);
		if (parentPreset == null && parentPresetId != null) {
			throw new BadRequestException("No Preset with id " + parentPresetId);
		}
		createPresetToPreset.setParentPreset(parentPreset);

		String childPresetId = createPresetToPreset.getChildPresetId();
		Preset childPreset = childPresetId == null ? null : getByIdOrNull(childPresetId, Preset.class, null, securityContext);
		if (childPreset == null && childPresetId != null) {
			throw new BadRequestException("No Preset with id " + childPresetId);
		}
		createPresetToPreset.setChildPreset(childPreset);
	}

}
