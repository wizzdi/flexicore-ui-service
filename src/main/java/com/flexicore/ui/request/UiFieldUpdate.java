package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.UiField;

public class UiFieldUpdate extends UiFieldCreate {
	private String id;
	private UiField uiField;

	public UiFieldUpdate() {
	}

	public String getId() {
		return id;
	}

	public UiFieldUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public UiField getUiField() {
		return uiField;
	}

	public UiFieldUpdate setUiField(UiField uiField) {
		this.uiField = uiField;
		return this;
	}

}
