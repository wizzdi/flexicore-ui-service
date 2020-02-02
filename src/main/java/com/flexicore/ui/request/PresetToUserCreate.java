package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.flexicore.ui.model.Preset;

public class PresetToUserCreate extends PresetToEntityCreate{


    private String userId;
    @JsonIgnore
    private User user;


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
}
