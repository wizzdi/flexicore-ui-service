package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Category;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.ui.model.Preset;

public class UiFieldCreate extends BaseclassCreate {
	private String presetId;
	@JsonIgnore
	private Preset preset;
	private Integer priority;
	private Boolean visible;
	private Boolean dynamicField;
	private String categoryName;
	@JsonIgnore
	private Category category;
	private String displayName;

	public UiFieldCreate() {
	}


	public String getPresetId() {
		return presetId;
	}

	public <T extends UiFieldCreate> T setPresetId(String presetId) {
		this.presetId = presetId;
		return (T) this;
	}
	@JsonIgnore
	public Preset getPreset() {
		return preset;
	}

	public <T extends UiFieldCreate> T setPreset(Preset preset) {
		this.preset = preset;
		return (T) this;
	}

	public Integer getPriority() {
		return priority;
	}

	public <T extends UiFieldCreate> T setPriority(Integer priority) {
		this.priority = priority;
		return (T) this;
	}

	public Boolean getVisible() {
		return visible;
	}

	public <T extends UiFieldCreate> T setVisible(Boolean visible) {
		this.visible = visible;
		return (T) this;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public <T extends UiFieldCreate> T setCategoryName(String categoryName) {
		this.categoryName = categoryName;
		return (T) this;
	}
	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	public <T extends UiFieldCreate> T setCategory(Category category) {
		this.category = category;
		return (T) this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public <T extends UiFieldCreate> T setDisplayName(String displayName) {
		this.displayName = displayName;
		return (T) this;
	}

	public Boolean getDynamicField() {
		return dynamicField;
	}

	public <T extends UiFieldCreate> T setDynamicField(Boolean dynamicField) {
		this.dynamicField = dynamicField;
		return (T) this;
	}
}
