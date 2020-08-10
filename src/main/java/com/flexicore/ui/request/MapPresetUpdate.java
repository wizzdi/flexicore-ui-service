package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.MapPreset;

public class MapPresetUpdate extends MapPresetCreate {
	private String id;
	@JsonIgnore
	private MapPreset mapPreset;

	public String getId() {
		return id;
	}

	public MapPresetUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public MapPreset getMapPreset() {
		return mapPreset;
	}

	public MapPresetUpdate setMapPreset(
			MapPreset mapPreset) {
		this.mapPreset = mapPreset;
		return this;
	}
}
