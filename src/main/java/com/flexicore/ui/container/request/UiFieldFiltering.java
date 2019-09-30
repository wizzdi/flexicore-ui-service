package com.flexicore.ui.container.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.ui.model.GridPreset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UiFieldFiltering extends FilteringInformationHolder {

    private Set<String> gridPresetIds=new HashSet<>();
    @JsonIgnore
    private List<GridPreset> gridPresets;

    public Set<String> getGridPresetIds() {
        return gridPresetIds;
    }

    public <T extends UiFieldFiltering> T setGridPresetIds(Set<String> gridPresetIds) {
        this.gridPresetIds = gridPresetIds;
        return (T) this;
    }

    @JsonIgnore
    public List<GridPreset> getGridPresets() {
        return gridPresets;
    }

    public <T extends UiFieldFiltering> T setGridPresets(List<GridPreset> gridPresets) {
        this.gridPresets = gridPresets;
        return (T) this;
    }
}
