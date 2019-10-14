package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToTenant;

public class PresetToTenantUpdate extends PresetToTenantCreate{

    private String linkId;
    @JsonIgnore
    private PresetToTenant presetToTenant;

    public String getLinkId() {
        return linkId;
    }

    public <T extends PresetToTenantUpdate> T setLinkId(String linkId) {
        this.linkId = linkId;
        return (T) this;
    }

    @JsonIgnore
    public PresetToTenant getPresetToTenant() {
        return presetToTenant;
    }

    public <T extends PresetToTenantUpdate> T setPresetToTenant(PresetToTenant presetToTenant) {
        this.presetToTenant = presetToTenant;
        return (T) this;
    }
}
