package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Preset;

public class UpdatePreset extends CreatePreset {

    private String id;
    @JsonIgnore
    private Preset preset;

    public String getId() {
        return id;
    }

    public UpdatePreset setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public UpdatePreset setPreset(Preset preset) {
        this.preset = preset;
        return this;
    }
}
