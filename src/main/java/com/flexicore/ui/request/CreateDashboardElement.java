package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Dashboard;

public class CreateDashboardElement {
    private String name;
    private String description;
    private String dashboardId;
    @JsonIgnore
    private Dashboard dashboard;
    private String contextString;

    public String getName() {
        return name;
    }

    public <T extends CreateDashboardElement> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends CreateDashboardElement> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public <T extends CreateDashboardElement> T setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
        return (T) this;
    }

    @JsonIgnore
    public Dashboard getDashboard() {
        return dashboard;
    }


    public <T extends CreateDashboardElement> T setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
        return (T) this;
    }

    public String getContextString() {
        return contextString;
    }

    public <T extends CreateDashboardElement> T setContextString(String contextString) {
        this.contextString = contextString;
        return (T) this;
    }
}
