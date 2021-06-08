package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;

public class PresetToTenantCreate extends PresetToEntityCreate {

	private String preferredTenantId;
	@JsonIgnore
	private SecurityTenant preferredTenant;

	public String getPreferredTenantId() {
		return preferredTenantId;
	}

	public <T extends PresetToTenantCreate> T setPreferredTenantId(
			String preferredTenantId) {
		this.preferredTenantId = preferredTenantId;
		return (T) this;
	}

	@JsonIgnore
	public SecurityTenant getPreferredTenant() {
		return preferredTenant;
	}

	public <T extends PresetToTenantCreate> T setPreferredTenant(
			SecurityTenant preferredTenant) {
		this.preferredTenant = preferredTenant;
		return (T) this;
	}

}
