package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToPreset;

public class PresetToPresetUpdate extends PresetToPresetCreate {

	private String id;
	@JsonIgnore
	private PresetToPreset presetToPreset;

	public String getId() {
		return id;
	}

	public PresetToPresetUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public PresetToPreset getPresetToPreset() {
		return presetToPreset;
	}

	public PresetToPresetUpdate setPresetToPreset(PresetToPreset presetToPreset) {
		this.presetToPreset = presetToPreset;
		return this;
	}
}
