package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Tenant;
import com.flexicore.ui.model.Preset;

public class LinkPresetToTenant {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String tenantId;
    @JsonIgnore
    private Tenant tenant;
    private Integer priority;



    public String getPresetId() {
        return presetId;
    }

    public LinkPresetToTenant setPresetId(String presetId) {
        this.presetId = presetId;
        return this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public LinkPresetToTenant setPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public LinkPresetToTenant setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public LinkPresetToTenant setTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public LinkPresetToTenant setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }
}
