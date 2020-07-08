package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.GridPreset;

import java.util.ArrayList;
import java.util.List;

public class MassCreateUiFields {

	private String gridPresetId;
	@JsonIgnore
	private GridPreset gridPreset;

	private List<UiFieldCreate> uiFields = new ArrayList<>();

	public String getGridPresetId() {
		return gridPresetId;
	}

	public <T extends MassCreateUiFields> T setGridPresetId(String gridPresetId) {
		this.gridPresetId = gridPresetId;
		return (T) this;
	}

	@JsonIgnore
	public GridPreset getGridPreset() {
		return gridPreset;
	}

	public <T extends MassCreateUiFields> T setGridPreset(GridPreset gridPreset) {
		this.gridPreset = gridPreset;
		return (T) this;
	}

	public List<UiFieldCreate> getUiFields() {
		return uiFields;
	}

	public <T extends MassCreateUiFields> T setUiFields(
			List<UiFieldCreate> uiFields) {
		this.uiFields = uiFields;
		return (T) this;
	}
}
