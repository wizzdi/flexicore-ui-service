package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.GridPreset;

public class GridPresetUpdate extends GridPresetCreate {

    private String id;
    @JsonIgnore
    private GridPreset preset;

    public String getId() {
        return id;
    }

    public GridPresetUpdate setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public GridPreset getPreset() {
        return preset;
    }

    public GridPresetUpdate setPreset(GridPreset preset) {
        this.preset = preset;
        return this;
    }
}
