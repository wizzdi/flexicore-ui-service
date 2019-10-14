package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Category;
import com.flexicore.ui.model.GridPreset;

public class UiFieldCreate {
    private String name;
    private String description;
    private String gridPresetId;
    @JsonIgnore
    private GridPreset gridPreset;
    private Integer priority;
    private Boolean visible;
    private String categoryName;
    @JsonIgnore
    private Category category;
    private String displayName;
    private Boolean sortable;
    private Boolean filterable;

    public UiFieldCreate() {
    }


    public String getName() {
        return name;
    }

    public UiFieldCreate setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UiFieldCreate setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getGridPresetId() {
        return gridPresetId;
    }

    public UiFieldCreate setGridPresetId(String gridPresetId) {
        this.gridPresetId = gridPresetId;
        return this;
    }

    @JsonIgnore
    public GridPreset getGridPreset() {
        return gridPreset;
    }

    public UiFieldCreate setGridPreset(GridPreset gridPreset) {
        this.gridPreset = gridPreset;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public UiFieldCreate setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Boolean getVisible() {
        return visible;
    }

    public UiFieldCreate setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public UiFieldCreate setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UiFieldCreate setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @JsonIgnore
    public Category getCategory() {
        return category;
    }

    public UiFieldCreate setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public UiFieldCreate setSortable(Boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    public Boolean getFilterable() {
        return filterable;
    }

    public UiFieldCreate setFilterable(Boolean filterable) {
        this.filterable = filterable;
        return this;
    }
}
