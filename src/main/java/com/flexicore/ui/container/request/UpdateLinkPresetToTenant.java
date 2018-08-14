package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToTenant;

public class UpdateLinkPresetToTenant {

    private String linkId;
    @JsonIgnore
    private PresetToTenant presetToTenant;
    private Boolean enabled;
    private Integer priority;

    public UpdateLinkPresetToTenant() {
    }

    public UpdateLinkPresetToTenant(LinkPresetToTenant other, PresetToTenant presetToTenant) {
        this.enabled = true;
        this.priority = other.getPriority();
        this.presetToTenant = presetToTenant;
    }

    public String getLinkId() {
        return linkId;
    }

    public UpdateLinkPresetToTenant setLinkId(String linkId) {
        this.linkId = linkId;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UpdateLinkPresetToTenant setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public UpdateLinkPresetToTenant setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }


    @JsonIgnore
    public PresetToTenant getPresetToTenant() {
        return presetToTenant;
    }

    public UpdateLinkPresetToTenant setPresetToTenant(PresetToTenant presetToTenant) {
        this.presetToTenant = presetToTenant;
        return this;
    }
}
