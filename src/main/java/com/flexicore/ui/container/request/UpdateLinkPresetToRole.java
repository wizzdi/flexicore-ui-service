package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToRole;

public class UpdateLinkPresetToRole {

    private String linkId;
    @JsonIgnore
    private PresetToRole presetToRole;
    private Boolean enabled;
    private Integer priority;

    public UpdateLinkPresetToRole() {
    }

    public UpdateLinkPresetToRole(LinkPresetToRole other, PresetToRole presetToRole) {
        this.enabled = true;
        this.priority = other.getPriority();
        this.presetToRole = presetToRole;
    }

    public String getLinkId() {
        return linkId;
    }

    public UpdateLinkPresetToRole setLinkId(String linkId) {
        this.linkId = linkId;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UpdateLinkPresetToRole setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public UpdateLinkPresetToRole setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }


    @JsonIgnore
    public PresetToRole getPresetToRole() {
        return presetToRole;
    }

    public UpdateLinkPresetToRole setPresetToRole(PresetToRole presetToRole) {
        this.presetToRole = presetToRole;
        return this;
    }
}
