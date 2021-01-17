package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Preset;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetToPresetFiltering extends PresetFiltering {

	private Set<String> parentPrestIds=new HashSet<>();
	@JsonIgnore
	private List<Preset> parentPresets;

	private Set<String> childPresetIds=new HashSet<>();
	@JsonIgnore
	private List<Preset> childPresets;

	public Set<String> getParentPrestIds() {
		return parentPrestIds;
	}

	public <T extends PresetToPresetFiltering> T setParentPrestIds(Set<String> parentPrestIds) {
		this.parentPrestIds = parentPrestIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Preset> getParentPresets() {
		return parentPresets;
	}

	public <T extends PresetToPresetFiltering> T setParentPresets(List<Preset> parentPresets) {
		this.parentPresets = parentPresets;
		return (T) this;
	}

	public Set<String> getChildPresetIds() {
		return childPresetIds;
	}

	public <T extends PresetToPresetFiltering> T setChildPresetIds(Set<String> childPresetIds) {
		this.childPresetIds = childPresetIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Preset> getChildPresets() {
		return childPresets;
	}

	public <T extends PresetToPresetFiltering> T setChildPresets(List<Preset> childPresets) {
		this.childPresets = childPresets;
		return (T) this;
	}
}
