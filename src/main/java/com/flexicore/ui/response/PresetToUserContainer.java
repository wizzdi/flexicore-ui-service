package com.flexicore.ui.response;

import com.flexicore.model.User;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToUser;


public class PresetToUserContainer {
    private String id;
    private boolean enabled;
    private int priority;
    private Preset preset;
    private User user;

    public PresetToUserContainer(PresetToUser other) {
        this.id = other.getId();
        this.enabled = other.isEnabled();
        this.priority = other.getPriority();
        this.preset = other.getLeftside();
        this.user = other.getRightside();
    }


    public String getId() {
        return id;
    }

    public <T extends PresetToUserContainer> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public <T extends PresetToUserContainer> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public int getPriority() {
        return priority;
    }

    public <T extends PresetToUserContainer> T setPriority(int priority) {
        this.priority = priority;
        return (T) this;
    }

    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToUserContainer> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public User getUser() {
        return user;
    }

    public <T extends PresetToUserContainer> T setUser(User user) {
        this.user = user;
        return (T) this;
    }
}
