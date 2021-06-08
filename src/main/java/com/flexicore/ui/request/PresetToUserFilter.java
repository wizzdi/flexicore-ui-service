package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetToUserFilter extends PresetLinkFilter {

	private Set<String> userIds = new HashSet<>();
	@JsonIgnore
	private List<SecurityUser> users;

	public <T extends PresetToUserFilter> T setUserIds(Set<String> userIds) {
		this.userIds = userIds;
		return (T) this;
	}

	@JsonIgnore
	public List<SecurityUser> getUsers() {
		return users;
	}

	public <T extends PresetToUserFilter> T setUsers(List<SecurityUser> users) {
		this.users = users;
		return (T) this;
	}

	public Set<String> getUserIds() {
		return userIds;
	}
}
