package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToRole;

public class PresetToRoleUpdate extends PresetToRoleCreate {

    private String linkId;
    @JsonIgnore
    private PresetToRole presetToRole;


    public String getLinkId() {
        return linkId;
    }

    public <T extends PresetToRoleUpdate> T setLinkId(String linkId) {
        this.linkId = linkId;
        return (T) this;
    }

    @JsonIgnore
    public PresetToRole getPresetToRole() {
        return presetToRole;
    }

    public <T extends PresetToRoleUpdate> T setPresetToRole(PresetToRole presetToRole) {
        this.presetToRole = presetToRole;
        return (T) this;
    }
}
