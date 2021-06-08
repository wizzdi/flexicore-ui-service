package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;
import com.flexicore.ui.model.Preset;

public class PresetToUserCreate extends PresetToEntityCreate {

	private String userId;
	@JsonIgnore
	private SecurityUser securityUser;

	public String getUserId() {
		return userId;
	}

	public <T extends PresetToUserCreate> T setUserId(String userId) {
		this.userId = userId;
		return (T) this;
	}

	@JsonIgnore
	public SecurityUser getUser() {
		return securityUser;
	}

	public <T extends PresetToUserCreate> T setUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
		return (T) this;
	}
}
