package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Category;
import com.flexicore.ui.model.GridPreset;

public class CreateUiField {
    private String name;
    private String description;
    private String presetId;
    @JsonIgnore
    private GridPreset preset;
    private Integer priority;
    private Boolean visible;
    private String categoryName;
    @JsonIgnore
    private Category category;
    private String displayName;
    private Boolean sortable;
    private Boolean filterable;

    public CreateUiField() {
    }


    public String getName() {
        return name;
    }

    public CreateUiField setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateUiField setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPresetId() {
        return presetId;
    }

    public CreateUiField setPresetId(String presetId) {
        this.presetId = presetId;
        return this;
    }

    @JsonIgnore
    public GridPreset getPreset() {
        return preset;
    }

    public CreateUiField setPreset(GridPreset preset) {
        this.preset = preset;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public CreateUiField setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }


    public Boolean getVisible() {
        return visible;
    }

    public CreateUiField setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public CreateUiField setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CreateUiField setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @JsonIgnore
    public Category getCategory() {
        return category;
    }

    public CreateUiField setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public CreateUiField setSortable(Boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    public Boolean getFilterable() {
        return filterable;
    }

    public CreateUiField setFilterable(Boolean filterable) {
        this.filterable = filterable;
        return this;
    }
}
