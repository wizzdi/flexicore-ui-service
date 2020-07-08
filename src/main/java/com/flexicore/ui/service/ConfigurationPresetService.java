package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.data.ConfigurationPresetRepository;
import com.flexicore.ui.model.ConfigurationPreset;
import com.flexicore.ui.request.CreateConfigurationPreset;
import com.flexicore.ui.request.ConfigurationPresetFiltering;
import com.flexicore.ui.request.UpdateConfigurationPreset;

import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class ConfigurationPresetService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private ConfigurationPresetRepository configurationPresetRepository;

	@PluginInfo(version = 1)
	@Autowired
	private PresetService presetService;

	public ConfigurationPreset updateConfigurationPreset(
			UpdateConfigurationPreset updateConfigurationPreset,
			SecurityContext securityContext) {
		if (updateConfigurationPresetNoMerge(updateConfigurationPreset,
				updateConfigurationPreset.getConfigurationPreset())) {
			configurationPresetRepository.merge(updateConfigurationPreset
					.getConfigurationPreset());
		}
		return updateConfigurationPreset.getConfigurationPreset();
	}

	public boolean updateConfigurationPresetNoMerge(
			CreateConfigurationPreset createConfigurationPreset,
			ConfigurationPreset configurationPreset) {
		boolean update = presetService.updatePresetNoMerge(
				createConfigurationPreset, configurationPreset);
		if (createConfigurationPreset.getConfigurationUI() != null
				&& !createConfigurationPreset.getConfigurationUI().equals(
						configurationPreset.getConfigurationUI())) {
			update = true;
			configurationPreset.setConfigurationUI(createConfigurationPreset
					.getConfigurationUI());
		}
		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return configurationPresetRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<ConfigurationPreset> getAllConfigurationPresets(
			ConfigurationPresetFiltering configurationPresetFiltering,
			SecurityContext securityContext) {
		List<ConfigurationPreset> list = listAllConfigurationPresets(
				configurationPresetFiltering, securityContext);
		long count = configurationPresetRepository
				.countAllConfigurationPresets(configurationPresetFiltering,
						securityContext);
		return new PaginationResponse<>(list, configurationPresetFiltering,
				count);
	}

	public List<ConfigurationPreset> listAllConfigurationPresets(
			ConfigurationPresetFiltering configurationPresetFiltering,
			SecurityContext securityContext) {
		return configurationPresetRepository.listAllConfigurationPresets(
				configurationPresetFiltering, securityContext);
	}

	public ConfigurationPreset createConfigurationPreset(
			CreateConfigurationPreset createConfigurationPreset,
			SecurityContext securityContext) {
		ConfigurationPreset configurationPreset = createConfigurationPresetNoMerge(
				createConfigurationPreset, securityContext);
		configurationPresetRepository.merge(configurationPreset);
		return configurationPreset;

	}

	private ConfigurationPreset createConfigurationPresetNoMerge(
			CreateConfigurationPreset createConfigurationPreset,
			SecurityContext securityContext) {
		ConfigurationPreset configurationPreset = ConfigurationPreset.s()
				.CreateUnchecked(createConfigurationPreset.getName(),
						securityContext);
		configurationPreset.Init();
		updateConfigurationPresetNoMerge(createConfigurationPreset,
				configurationPreset);
		return configurationPreset;
	}

	public void validate(CreateConfigurationPreset createConfigurationPreset,
			SecurityContext securityContext) {

	}
}
