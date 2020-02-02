package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.ui.model.Preset;

public class PresetToEntityCreate extends BaseclassCreate {

    private String presetId;
    @JsonIgnore
    private Preset preset;

    private Integer priority;
    private Boolean enabled;

    public String getPresetId() {
        return presetId;
    }

    public <T extends PresetToEntityCreate> T setPresetId(String presetId) {
        this.presetId = presetId;
        return (T) this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToEntityCreate> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public Integer getPriority() {
        return priority;
    }

    public <T extends PresetToEntityCreate> T setPriority(Integer priority) {
        this.priority = priority;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends PresetToEntityCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
