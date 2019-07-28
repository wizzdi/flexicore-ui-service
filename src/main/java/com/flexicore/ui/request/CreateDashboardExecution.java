package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.ui.model.DashboardElement;

public class CreateDashboardExecution {

    private String name;
    private String description;
    private String dashboardElementId;
    @JsonIgnore
    private DashboardElement dashboardElement;
    private String dynamicExecutionId;
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    public String getName() {
        return name;
    }

    public <T extends CreateDashboardExecution> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends CreateDashboardExecution> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getDashboardElementId() {
        return dashboardElementId;
    }

    public <T extends CreateDashboardExecution> T setDashboardElementId(String dashboardElementId) {
        this.dashboardElementId = dashboardElementId;
        return (T) this;
    }
    @JsonIgnore

    public DashboardElement getDashboardElement() {
        return dashboardElement;
    }

    public <T extends CreateDashboardExecution> T setDashboardElement(DashboardElement dashboardElement) {
        this.dashboardElement = dashboardElement;
        return (T) this;
    }

    public String getDynamicExecutionId() {
        return dynamicExecutionId;
    }

    public <T extends CreateDashboardExecution> T setDynamicExecutionId(String dynamicExecutionId) {
        this.dynamicExecutionId = dynamicExecutionId;
        return (T) this;
    }
    @JsonIgnore

    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public <T extends CreateDashboardExecution> T setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return (T) this;
    }
}
