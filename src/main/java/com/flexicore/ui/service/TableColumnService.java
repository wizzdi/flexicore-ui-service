package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.data.TableColumnRepository;
import com.flexicore.ui.model.TableColumn;
import com.flexicore.ui.request.TableColumnCreate;
import com.flexicore.ui.request.TableColumnFiltering;
import com.flexicore.ui.request.TableColumnUpdate;

import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class TableColumnService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private TableColumnRepository tableColumnRepository;

	@PluginInfo(version = 1)
	@Autowired
	private UiFieldService uiFieldService;

	public TableColumn TableColumnUpdate(TableColumnUpdate TableColumnUpdate,
			SecurityContext securityContext) {
		if (TableColumnUpdateNoMerge(TableColumnUpdate,
				TableColumnUpdate.getTableColumn())) {
			tableColumnRepository.merge(TableColumnUpdate.getTableColumn());
		}
		return TableColumnUpdate.getTableColumn();
	}

	public boolean TableColumnUpdateNoMerge(
			TableColumnCreate tableColumnCreate, TableColumn tableColumn) {
		boolean update = uiFieldService.updateUiFieldNoMerge(tableColumnCreate,
				tableColumn);

		if (tableColumnCreate.getSortable() != null
				&& tableColumnCreate.getSortable() != tableColumn.isSortable()) {
			update = true;
			tableColumn.setSortable(tableColumnCreate.getSortable());
		}

		if (tableColumnCreate.getFilterable() != null
				&& tableColumnCreate.getFilterable() != tableColumn
						.isFilterable()) {
			update = true;
			tableColumn.setFilterable(tableColumnCreate.getFilterable());
		}

		if (tableColumnCreate.getDefaultColumnWidth() != null
				&& tableColumnCreate.getDefaultColumnWidth() != tableColumn
						.getDefaultColumnWidth()) {
			update = true;
			tableColumn.setDefaultColumnWidth(tableColumnCreate
					.getDefaultColumnWidth());
		}

		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return tableColumnRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public List<TableColumn> listAllTableColumns(
			TableColumnFiltering tableColumnFiltering,
			SecurityContext securityContext) {
		return tableColumnRepository.listAllTableColumns(tableColumnFiltering,
				securityContext);
	}

	public TableColumn createTableColumn(TableColumnCreate createTableColumn,
			SecurityContext securityContext) {
		TableColumn tableColumn = createTableColumnNoMerge(createTableColumn,
				securityContext);
		tableColumnRepository.merge(tableColumn);
		return tableColumn;

	}

	public TableColumn createTableColumnNoMerge(
			TableColumnCreate createTableColumn, SecurityContext securityContext) {
		TableColumn tableColumn = new TableColumn(createTableColumn.getName(),
				securityContext);
		TableColumnUpdateNoMerge(createTableColumn, tableColumn);
		return tableColumn;
	}

	public PaginationResponse<TableColumn> getAllTableColumns(
			TableColumnFiltering tableColumnFiltering,
			SecurityContext securityContext) {
		List<TableColumn> list = listAllTableColumns(tableColumnFiltering,
				securityContext);
		long count = tableColumnRepository.countAllTableColumns(
				tableColumnFiltering, securityContext);
		return new PaginationResponse<>(list, tableColumnFiltering, count);
	}

	public void validate(TableColumnCreate createTableColumn,
			SecurityContext securityContext) {
		uiFieldService.validate(createTableColumn, securityContext);
	}

	public void validate(TableColumnFiltering tableColumnFiltering,
			SecurityContext securityContext) {
		uiFieldService.validate(tableColumnFiltering, securityContext);
	}

	public TableColumnCreate getTableColumnCreate(TableColumn tableColumn) {
		return new TableColumnCreate()
				.setFilterable(tableColumn.isFilterable())
				.setSortable(tableColumn.isSortable())
				.setDefaultColumnWidth(tableColumn.getDefaultColumnWidth())
				.setPreset(tableColumn.getPreset())
				.setCategory(tableColumn.getCategory())
				.setDisplayName(tableColumn.getDisplayName())
				.setVisible(tableColumn.isVisible())
				.setPriority(tableColumn.getPriority())
				.setDescription(tableColumn.getDescription())
				.setName(tableColumn.getName());
	}
}
