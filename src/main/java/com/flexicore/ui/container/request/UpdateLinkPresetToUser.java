package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToUser;

public class UpdateLinkPresetToUser {

    private String linkId;
    @JsonIgnore
    private PresetToUser presetToUser;
    private Boolean enabled;
    private Integer priority;

    public UpdateLinkPresetToUser() {
    }

    public UpdateLinkPresetToUser(LinkPresetToUser other,PresetToUser presetToUser) {
        this.enabled = true;
        this.priority = other.getPriority();
        this.presetToUser=presetToUser;
    }

    public String getLinkId() {
        return linkId;
    }

    public UpdateLinkPresetToUser setLinkId(String linkId) {
        this.linkId = linkId;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public UpdateLinkPresetToUser setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public UpdateLinkPresetToUser setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }


    @JsonIgnore
    public PresetToUser getPresetToUser() {
        return presetToUser;
    }

    public UpdateLinkPresetToUser setPresetToUser(PresetToUser presetToUser) {
        this.presetToUser = presetToUser;
        return this;
    }
}
