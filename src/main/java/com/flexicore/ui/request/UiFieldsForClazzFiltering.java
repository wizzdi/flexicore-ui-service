package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.request.PaginationFilter;

public class UiFieldsForClazzFiltering extends PaginationFilter  {

	private String clazzName;
	@JsonIgnore
	private Clazz clazz;

	public String getClazzName() {
		return clazzName;
	}

	public UiFieldsForClazzFiltering setClazzName(String clazzName) {
		this.clazzName = clazzName;
		return this;
	}

	@JsonIgnore
	public Clazz getClazz() {
		return clazz;
	}

	public UiFieldsForClazzFiltering setClazz(Clazz clazz) {
		this.clazz = clazz;
		return this;
	}
}
