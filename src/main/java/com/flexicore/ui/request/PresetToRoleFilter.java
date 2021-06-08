package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.security.request.PaginationFilter;
import com.flexicore.model.Role;
import com.flexicore.ui.model.Preset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetToRoleFilter extends PresetLinkFilter {

	private Set<String> roleIds = new HashSet<>();
	@JsonIgnore
	private List<Role> roles;

	public <T extends PresetToRoleFilter> T setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}

	public <T extends PresetToRoleFilter> T setRoles(List<Role> roles) {
		this.roles = roles;
		return (T) this;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}
}
