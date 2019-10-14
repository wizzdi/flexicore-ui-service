package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.flexicore.ui.model.Preset;

public class PresetToUserCreate {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String userId;
    @JsonIgnore
    private User user;
    private Integer priority;
    private Boolean enabled;

    public String getPresetId() {
        return presetId;
    }

    public <T extends PresetToUserCreate> T setPresetId(String presetId) {
        this.presetId = presetId;
        return (T) this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToUserCreate> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends PresetToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public <T extends PresetToUserCreate> T setUser(User user) {
        this.user = user;
        return (T) this;
    }

    public Integer getPriority() {
        return priority;
    }

    public <T extends PresetToUserCreate> T setPriority(Integer priority) {
        this.priority = priority;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends PresetToUserCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
