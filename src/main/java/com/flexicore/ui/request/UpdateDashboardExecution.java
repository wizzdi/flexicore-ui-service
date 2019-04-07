package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.DashboardExecution;

public class UpdateDashboardExecution extends CreateDashboardExecution{
    private String id;
    @JsonIgnore
    private DashboardExecution dashboardExecution;

    public String getId() {
        return id;
    }

    public <T extends UpdateDashboardExecution> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public DashboardExecution getDashboardExecution() {
        return dashboardExecution;
    }

    public <T extends UpdateDashboardExecution> T setDashboardExecution(DashboardExecution dashboardExecution) {
        this.dashboardExecution = dashboardExecution;
        return (T) this;
    }
}
