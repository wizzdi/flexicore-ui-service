package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.ui.model.Preset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetLinkFilter extends FilteringInformationHolder {

	private Set<String> presetIds = new HashSet<>();
	@JsonIgnore
	private List<Preset> presets;

	public Set<String> getPresetIds() {
		return presetIds;
	}

	public <T extends PresetLinkFilter> T setPresetIds(Set<String> presetIds) {
		this.presetIds = presetIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Preset> getPresets() {
		return presets;
	}

	public <T extends PresetLinkFilter> T setPresets(List<Preset> presets) {
		this.presets = presets;
		return (T) this;
	}

}
