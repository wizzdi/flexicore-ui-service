package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToUser;

public class PresetToUserUpdate extends PresetToUserCreate{

    private String linkId;
    @JsonIgnore
    private PresetToUser presetToUser;

    public String getLinkId() {
        return linkId;
    }

    public <T extends PresetToUserUpdate> T setLinkId(String linkId) {
        this.linkId = linkId;
        return (T) this;
    }

    @JsonIgnore
    public PresetToUser getPresetToUser() {
        return presetToUser;
    }

    public <T extends PresetToUserUpdate> T setPresetToUser(PresetToUser presetToUser) {
        this.presetToUser = presetToUser;
        return (T) this;
    }
}
