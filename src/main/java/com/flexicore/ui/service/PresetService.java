package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.ui.data.PresetRepository;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.request.CreatePreset;
import com.flexicore.ui.request.PresetFiltering;
import com.flexicore.ui.request.UpdatePreset;

import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class PresetService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private PresetRepository presetRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public Preset updatePreset(UpdatePreset updatePreset,
			SecurityContext securityContext) {
		if (updatePresetNoMerge(updatePreset, updatePreset.getPreset())) {
			presetRepository.merge(updatePreset.getPreset());
		}
		return updatePreset.getPreset();
	}

	public boolean updatePresetNoMerge(CreatePreset createPreset, Preset preset) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(createPreset,preset);

		if (createPreset.getExternalId() != null && (preset.getExternalId() == null || !createPreset.getExternalId().equals(preset.getExternalId()))) {
			preset.setExternalId(createPreset.getExternalId());

			update = true;
		}

		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
		return presetRepository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public PaginationResponse<Preset> getAllPresets(
			PresetFiltering presetFiltering, SecurityContext securityContext) {
		List<Preset> list = listAllPresets(presetFiltering, securityContext);
		long count = presetRepository.countAllPresets(presetFiltering, securityContext);
		return new PaginationResponse<>(list, presetFiltering, count);
	}

	public List<Preset> listAllPresets(PresetFiltering presetFiltering,
			SecurityContext securityContext) {
		return presetRepository.listAllPresets(presetFiltering, securityContext);
	}

	public Preset createPreset(CreatePreset createPreset,
			SecurityContext securityContext) {
		Preset preset = createPresetNoMerge(createPreset, securityContext);
		presetRepository.merge(preset);
		return preset;

	}

	private Preset createPresetNoMerge(CreatePreset createPreset,
			SecurityContext securityContext) {
		Preset preset = new Preset(createPreset.getName(), securityContext);
		updatePresetNoMerge(createPreset, preset);
		return preset;
	}

}
