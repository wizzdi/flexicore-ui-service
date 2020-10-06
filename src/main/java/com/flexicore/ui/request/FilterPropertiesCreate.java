package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.ui.model.GridPreset;

public class FilterPropertiesCreate extends BaseclassCreate {

    private String filterPath;
    private Boolean externalize;
    private String presetId;
    @JsonIgnore
    private GridPreset gridPreset;

    public Boolean getExternalize() {
        return externalize;
    }

    public <T extends FilterPropertiesCreate> T setExternalize(Boolean externalize) {
        this.externalize = externalize;
        return (T) this;
    }

    public String getPresetId() {
        return presetId;
    }

    public <T extends FilterPropertiesCreate> T setPresetId(String presetId) {
        this.presetId = presetId;
        return (T) this;
    }

    @JsonIgnore
    public GridPreset getGridPreset() {
        return gridPreset;
    }

    public <T extends FilterPropertiesCreate> T setGridPreset(GridPreset gridPreset) {
        this.gridPreset = gridPreset;
        return (T) this;
    }

    public String getFilterPath() {
        return filterPath;
    }

    public <T extends FilterPropertiesCreate> T setFilterPath(String filterPath) {
        this.filterPath = filterPath;
        return (T) this;
    }
}
