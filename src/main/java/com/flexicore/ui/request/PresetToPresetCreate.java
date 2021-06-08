package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.security.request.BasicCreate;
import com.flexicore.ui.model.Preset;

public class PresetToPresetCreate extends BasicCreate {

	private String parentPresetId;
	@JsonIgnore
	private Preset parentPreset;

	private String childPresetId;
	@JsonIgnore
	private Preset childPreset;
	private String parentPath;
	private String childPath;

	public String getParentPresetId() {
		return parentPresetId;
	}

	public <T extends PresetToPresetCreate> T setParentPresetId(
			String parentPresetId) {
		this.parentPresetId = parentPresetId;
		return (T) this;
	}

	@JsonIgnore
	public Preset getParentPreset() {
		return parentPreset;
	}

	public <T extends PresetToPresetCreate> T setParentPreset(
			Preset parentPreset) {
		this.parentPreset = parentPreset;
		return (T) this;
	}

	public String getChildPresetId() {
		return childPresetId;
	}

	public <T extends PresetToPresetCreate> T setChildPresetId(String childPresetId) {
		this.childPresetId = childPresetId;
		return (T) this;
	}

	@JsonIgnore
	public Preset getChildPreset() {
		return childPreset;
	}

	public <T extends PresetToPresetCreate> T setChildPreset(Preset childPreset) {
		this.childPreset = childPreset;
		return (T) this;
	}

	public String getParentPath() {
		return parentPath;
	}

	public <T extends PresetToPresetCreate> T setParentPath(String parentPath) {
		this.parentPath = parentPath;
		return (T) this;
	}

	public String getChildPath() {
		return childPath;
	}

	public <T extends PresetToPresetCreate> T setChildPath(String childPath) {
		this.childPath = childPath;
		return (T) this;
	}
}
