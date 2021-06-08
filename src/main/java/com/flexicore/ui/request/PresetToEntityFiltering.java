package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.ui.model.Preset;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PresetToEntityFiltering extends PaginationFilter {
	private Set<String> presetIds=new HashSet<>();
	@JsonIgnore
	private List<Preset> presets;

	private Set<String> entityIds=new HashSet<>();
	@JsonIgnore
	private List<Baseclass> entities;


	private Boolean enabled;

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends PresetToEntityFiltering> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public Set<String> getPresetIds() {
		return presetIds;
	}

	public <T extends PresetToEntityFiltering> T setPresetIds(Set<String> presetIds) {
		this.presetIds = presetIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Preset> getPresets() {
		return presets;
	}

	public <T extends PresetToEntityFiltering> T setPresets(List<Preset> presets) {
		this.presets = presets;
		return (T) this;
	}

	public Set<String> getEntityIds() {
		return entityIds;
	}

	public <T extends PresetToEntityFiltering> T setEntityIds(Set<String> entityIds) {
		this.entityIds = entityIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Baseclass> getEntities() {
		return entities;
	}

	public <T extends PresetToEntityFiltering> T setEntities(List<Baseclass> entities) {
		this.entities = entities;
		return (T) this;
	}
}
