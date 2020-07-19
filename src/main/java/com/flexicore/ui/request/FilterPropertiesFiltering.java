package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.ui.model.GridPreset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterPropertiesFiltering extends FilteringInformationHolder {

    private Set<String> presetIds=new HashSet<>();
    @JsonIgnore
    private List<GridPreset> gridPresets;

    public Set<String> getPresetIds() {
        return presetIds;
    }

    public <T extends FilterPropertiesFiltering> T setPresetIds(Set<String> presetIds) {
        this.presetIds = presetIds;
        return (T) this;
    }

    @JsonIgnore
    public List<GridPreset> getGridPresets() {
        return gridPresets;
    }

    public <T extends FilterPropertiesFiltering> T setGridPresets(List<GridPreset> gridPresets) {
        this.gridPresets = gridPresets;
        return (T) this;
    }
}
