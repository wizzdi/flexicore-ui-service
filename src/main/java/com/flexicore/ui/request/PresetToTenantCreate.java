package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Tenant;
import com.flexicore.ui.model.Preset;

public class PresetToTenantCreate {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String tenantId;
    @JsonIgnore
    private Tenant tenant;
    private Integer priority;
    private Boolean enabled;


    public String getPresetId() {
        return presetId;
    }

    public <T extends PresetToTenantCreate> T setPresetId(String presetId) {
        this.presetId = presetId;
        return (T) this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToTenantCreate> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends PresetToTenantCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends PresetToTenantCreate> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public Integer getPriority() {
        return priority;
    }

    public <T extends PresetToTenantCreate> T setPriority(Integer priority) {
        this.priority = priority;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends PresetToTenantCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
