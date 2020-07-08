package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.TableColumn;

public class TableColumnUpdate extends TableColumnCreate {
	private String id;
	private TableColumn tableColumn;

	public TableColumnUpdate() {
	}

	public String getId() {
		return id;
	}

	public TableColumnUpdate setId(String id) {
		this.id = id;
		return this;
	}

	@JsonIgnore
	public TableColumn getTableColumn() {
		return tableColumn;
	}

	public TableColumnUpdate setTableColumn(TableColumn tableColumn) {
		this.tableColumn = tableColumn;
		return this;
	}

}
