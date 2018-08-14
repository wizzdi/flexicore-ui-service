package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.ui.model.Preset;

public class LinkPresetToRole {

    private String presetId;
    @JsonIgnore
    private Preset preset;
    private String roleId;
    @JsonIgnore
    private Role role;
    private Integer priority;


    public String getPresetId() {
        return presetId;
    }

    public LinkPresetToRole setPresetId(String presetId) {
        this.presetId = presetId;
        return this;
    }

    @JsonIgnore
    public Preset getPreset() {
        return preset;
    }

    public LinkPresetToRole setPreset(Preset preset) {
        this.preset = preset;
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public LinkPresetToRole setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public LinkPresetToRole setRole(Role role) {
        this.role = role;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public LinkPresetToRole setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }
}
