package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.flexicore.ui.model.Preset;

public class LinkPresetToUser {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String userId;
    @JsonIgnore
    private User user;
    private Integer priority;



    public String getPresetId() {
        return presetId;
    }

    public LinkPresetToUser setPresetId(String presetId) {
        this.presetId = presetId;
        return this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public LinkPresetToUser setPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public LinkPresetToUser setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public LinkPresetToUser setUser(User user) {
        this.user = user;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public LinkPresetToUser setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }
}
