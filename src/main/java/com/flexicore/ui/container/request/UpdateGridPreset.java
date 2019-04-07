package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.GridPreset;

public class UpdateGridPreset extends CreateGridPreset {

    private String id;
    @JsonIgnore
    private GridPreset preset;

    public String getId() {
        return id;
    }

    public UpdateGridPreset setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public GridPreset getPreset() {
        return preset;
    }

    public UpdateGridPreset setPreset(GridPreset preset) {
        this.preset = preset;
        return this;
    }
}
