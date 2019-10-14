package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.ui.model.Preset;

public class PresetToRoleCreate {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String roleId;
    @JsonIgnore
    private Role role;
    private Integer priority;
    private Boolean enabled;


    public String getPresetId() {
        return presetId;
    }

    public <T extends PresetToRoleCreate> T setPresetId(String presetId) {
        this.presetId = presetId;
        return (T) this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public <T extends PresetToRoleCreate> T setPreset(Preset preset) {
        this.preset = preset;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends PresetToRoleCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends PresetToRoleCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public Integer getPriority() {
        return priority;
    }

    public <T extends PresetToRoleCreate> T setPriority(Integer priority) {
        this.priority = priority;
        return (T) this;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends PresetToRoleCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
