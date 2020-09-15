package com.flexicore.ui.request;

public class ConfigurationPresetCreate extends PresetCreate {

	private String configurationUI;

	public String getConfigurationUI() {
		return configurationUI;
	}

	public ConfigurationPresetCreate setConfigurationUI(String configurationUI) {
		this.configurationUI = configurationUI;
		return this;
	}
}
