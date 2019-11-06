package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Category;
import com.flexicore.ui.model.GridPreset;

public class ColumnCreate extends UiFieldCreate{
    private Boolean sortable;
    private Boolean filterable;
    private Double defaultColumnWidth;

    public ColumnCreate() {
    }


    public Boolean getSortable() {
        return sortable;
    }

    public <T extends ColumnCreate> T setSortable(Boolean sortable) {
        this.sortable = sortable;
        return (T) this;
    }

    public Boolean getFilterable() {
        return filterable;
    }

    public <T extends ColumnCreate> T setFilterable(Boolean filterable) {
        this.filterable = filterable;
        return (T) this;
    }

    public Double getDefaultColumnWidth() {
        return defaultColumnWidth;
    }

    public <T extends ColumnCreate> T setDefaultColumnWidth(Double defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
        return (T) this;
    }
}
