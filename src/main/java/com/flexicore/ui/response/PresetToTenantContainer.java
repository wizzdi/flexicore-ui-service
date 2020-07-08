package com.flexicore.ui.response;

import com.flexicore.model.Tenant;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToTenant;

public class PresetToTenantContainer {
	private String id;
	private boolean enabled;
	private int priority;
	private Preset preset;
	private Tenant tenant;

	public PresetToTenantContainer(PresetToTenant other) {
		this.id = other.getId();
		this.enabled = other.isEnabled();
		this.priority = other.getPriority();
		this.preset = other.getLeftside();
		this.tenant = other.getRightside();
	}

	public String getId() {
		return id;
	}

	public <T extends PresetToTenantContainer> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public <T extends PresetToTenantContainer> T setEnabled(boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public int getPriority() {
		return priority;
	}

	public <T extends PresetToTenantContainer> T setPriority(int priority) {
		this.priority = priority;
		return (T) this;
	}

	public Preset getPreset() {
		return preset;
	}

	public <T extends PresetToTenantContainer> T setPreset(Preset preset) {
		this.preset = preset;
		return (T) this;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public <T extends PresetToTenantContainer> T setTenant(Tenant tenant) {
		this.tenant = tenant;
		return (T) this;
	}
}
