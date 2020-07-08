package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.ui.model.Preset;

public class PresetToRoleCreate extends PresetToEntityCreate {

	private String roleId;
	@JsonIgnore
	private Role role;

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

}
