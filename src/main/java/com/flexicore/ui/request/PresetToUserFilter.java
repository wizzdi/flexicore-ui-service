package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetToUserFilter extends PresetLinkFilter {

	private Set<String> userIds = new HashSet<>();
	@JsonIgnore
	private List<User> users;

	public <T extends PresetToUserFilter> T setUserIds(Set<String> userIds) {
		this.userIds = userIds;
		return (T) this;
	}

	@JsonIgnore
	public List<User> getUsers() {
		return users;
	}

	public <T extends PresetToUserFilter> T setUsers(List<User> users) {
		this.users = users;
		return (T) this;
	}

	public Set<String> getUserIds() {
		return userIds;
	}
}
