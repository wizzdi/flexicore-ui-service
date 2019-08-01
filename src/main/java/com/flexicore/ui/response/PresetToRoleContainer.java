package com.flexicore.ui.response;

import com.flexicore.model.Role;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToRole;


public class PresetToRoleContainer {
    private String id;
    private boolean enabled;
    private int priority;
    private Preset preset;
    private Role role;

    public PresetToRoleContainer(PresetToRole other) {
        this.id = other.getId();
        this.enabled = other.isEnabled();
        this.priority = other.getPriority();
        this.preset = other.getLeftside();
        this.role = other.getRightside();
    }


    public String getId() {
        return id;
    }

    public <T extends PresetToRoleContainer> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public <T extends PresetToRoleContainer> T setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }

    public int getPriority() {
        return priority;
    }

    public <T extends PresetToRoleContainer> T setPriority(int priority) {
        this.priority = priority;
        return (T) this;
    }

    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToRoleContainer> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public Role getRole() {
        return role;
    }

    public <T extends PresetToRoleContainer> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }
}
