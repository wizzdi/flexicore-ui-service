package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.ConfigurationPreset;

public class UpdateConfigurationPreset extends CreateConfigurationPreset{
    private String id;
    @JsonIgnore
    private ConfigurationPreset configurationPreset;

    public String getId() {
        return id;
    }

    public UpdateConfigurationPreset setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public ConfigurationPreset getConfigurationPreset() {
        return configurationPreset;
    }

    public UpdateConfigurationPreset setConfigurationPreset(ConfigurationPreset configurationPreset) {
        this.configurationPreset = configurationPreset;
        return this;
    }
}
