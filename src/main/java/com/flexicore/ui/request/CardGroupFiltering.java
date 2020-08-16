package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.ui.model.Dashboard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardGroupFiltering extends FilteringInformationHolder {

    private Set<String> dashboardPresetIds=new HashSet<>();
    @JsonIgnore
    private List<Dashboard> dashboardPresets;


    public Set<String> getDashboardPresetIds() {
        return dashboardPresetIds;
    }

    public <T extends CardGroupFiltering> T setDashboardPresetIds(Set<String> dashboardPresetIds) {
        this.dashboardPresetIds = dashboardPresetIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Dashboard> getDashboardPresets() {
        return dashboardPresets;
    }

    public <T extends CardGroupFiltering> T setDashboardPresets(List<Dashboard> dashboardPresets) {
        this.dashboardPresets = dashboardPresets;
        return (T) this;
    }

    @Override
    public boolean supportingDynamic() {
        return true;
    }
}
