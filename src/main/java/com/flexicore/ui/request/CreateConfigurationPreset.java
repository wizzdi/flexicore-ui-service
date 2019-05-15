package com.flexicore.ui.request;

public class CreateConfigurationPreset extends CreatePreset{

    private String configurationUI;

    public String getConfigurationUI() {
        return configurationUI;
    }

    public CreateConfigurationPreset setConfigurationUI(String configurationUI) {
        this.configurationUI = configurationUI;
        return this;
    }
}
