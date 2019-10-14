package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.flexicore.ui.model.UiField;

public class LinkUiFieldRequest {

    private String clazzName;
    @JsonIgnore
    private Clazz clazz;
    private String uiFieldId;
    @JsonIgnore
    private UiField uiField;

    private int priority;
    private String context;
    private boolean visible;
    private String categoryName;
    private String displayName;

    public String getCategoryName() {
        return categoryName;
    }

    public LinkUiFieldRequest setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getClazzName() {
        return clazzName;
    }

    public LinkUiFieldRequest setClazzName(String clazzName) {
        this.clazzName = clazzName;
        return this;
    }

    public String getUiFieldId() {
        return uiFieldId;
    }

    public LinkUiFieldRequest setUiFieldId(String uiFieldId) {
        this.uiFieldId = uiFieldId;
        return this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return clazz;
    }

    public LinkUiFieldRequest setClazz(Clazz clazz) {
        this.clazz = clazz;
        return this;
    }
    @JsonIgnore
    public UiField getUiField() {
        return uiField;
    }

    public LinkUiFieldRequest setUiField(UiField uiField) {
        this.uiField = uiField;
        return this;
    }


    public int getPriority() {
        return priority;
    }

    public LinkUiFieldRequest setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public String getContext() {
        return context;
    }

    public LinkUiFieldRequest setContext(String context) {
        this.context = context;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public LinkUiFieldRequest setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public LinkUiFieldRequest setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
}
