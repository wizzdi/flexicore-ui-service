package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.ConfigurationPreset;

public class ConfigurationPresetUpdate extends ConfigurationPresetCreate {
	private String id;
	@JsonIgnore
	private ConfigurationPreset configurationPreset;

	public String getId() {
		return id;
	}

	public ConfigurationPresetUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public ConfigurationPreset getConfigurationPreset() {
		return configurationPreset;
	}

	public ConfigurationPresetUpdate setConfigurationPreset(
			ConfigurationPreset configurationPreset) {
		this.configurationPreset = configurationPreset;
		return this;
	}
}
