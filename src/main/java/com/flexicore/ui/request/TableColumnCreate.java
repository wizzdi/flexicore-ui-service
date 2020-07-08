package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Category;
import com.flexicore.ui.model.GridPreset;

public class TableColumnCreate extends UiFieldCreate {
	private Boolean sortable;
	private Boolean filterable;
	private Double defaultTableColumnWidth;

	public TableColumnCreate() {
	}

	public Boolean getSortable() {
		return sortable;
	}

	public <T extends TableColumnCreate> T setSortable(Boolean sortable) {
		this.sortable = sortable;
		return (T) this;
	}

	public Boolean getFilterable() {
		return filterable;
	}

	public <T extends TableColumnCreate> T setFilterable(Boolean filterable) {
		this.filterable = filterable;
		return (T) this;
	}

	public Double getDefaultTableColumnWidth() {
		return defaultTableColumnWidth;
	}

	public <T extends TableColumnCreate> T setDefaultTableColumnWidth(
			Double defaultTableColumnWidth) {
		this.defaultTableColumnWidth = defaultTableColumnWidth;
		return (T) this;
	}
}
