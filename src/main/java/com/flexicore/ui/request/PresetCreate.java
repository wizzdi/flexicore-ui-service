package com.flexicore.ui.request;

import com.flexicore.request.BaseclassCreate;

public class PresetCreate extends BaseclassCreate {

	private String externalId;

	public String getExternalId() {
		return externalId;
	}

	public <T extends PresetCreate> T setExternalId(String externalId) {
		this.externalId = externalId;
		return (T) this;
	}
}
