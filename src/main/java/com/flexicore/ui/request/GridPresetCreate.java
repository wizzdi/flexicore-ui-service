package com.flexicore.ui.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicExecution;

public class GridPresetCreate extends CreatePreset {

    private String relatedClassCanonicalName;
    private String dynamicExecutionId;
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    public String getRelatedClassCanonicalName() {
        return relatedClassCanonicalName;
    }

    public <T extends GridPresetCreate> T setRelatedClassCanonicalName(String relatedClassCanonicalName) {
        this.relatedClassCanonicalName = relatedClassCanonicalName;
        return (T) this;
    }

    public String getDynamicExecutionId() {
        return dynamicExecutionId;
    }

    public <T extends GridPresetCreate> T setDynamicExecutionId(String dynamicExecutionId) {
        this.dynamicExecutionId = dynamicExecutionId;
        return (T) this;
    }

    @JsonIgnore
    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public <T extends GridPresetCreate> T setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return (T) this;
    }

}
