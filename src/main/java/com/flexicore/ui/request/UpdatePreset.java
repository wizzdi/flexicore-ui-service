package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Preset;

public class UpdatePreset extends CreatePreset{
    private String id;
    @JsonIgnore
    private Preset preset;

    public String getId() {
        return id;
    }

    public <T extends UpdatePreset> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public <T extends UpdatePreset> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }
}
