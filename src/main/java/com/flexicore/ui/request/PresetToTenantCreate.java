package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Tenant;

public class PresetToTenantCreate extends PresetToEntityCreate {

	private String preferredTenantId;
	@JsonIgnore
	private Tenant preferredTenant;

	public String getPreferredTenantId() {
		return preferredTenantId;
	}

	public <T extends PresetToTenantCreate> T setPreferredTenantId(
			String preferredTenantId) {
		this.preferredTenantId = preferredTenantId;
		return (T) this;
	}

	@JsonIgnore
	public Tenant getPreferredTenant() {
		return preferredTenant;
	}

	public <T extends PresetToTenantCreate> T setPreferredTenant(
			Tenant preferredTenant) {
		this.preferredTenant = preferredTenant;
		return (T) this;
	}

}
