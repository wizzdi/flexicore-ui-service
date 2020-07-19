package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.FilterProperties;

public class FilterPropertiesUpdate extends FilterPropertiesCreate {

	private String id;
	@JsonIgnore
	private FilterProperties filterProperties;

	public String getId() {
		return id;
	}

	public FilterPropertiesUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public FilterProperties getFilterProperties() {
		return filterProperties;
	}

	public FilterPropertiesUpdate setFilterProperties(FilterProperties filterProperties) {
		this.filterProperties = filterProperties;
		return this;
	}
}
