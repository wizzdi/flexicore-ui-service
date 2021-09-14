package com.flexicore.ui.service;


import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.TableColumnRepository;
import com.flexicore.ui.model.TableColumn;
import com.flexicore.ui.request.TableColumnCreate;
import com.flexicore.ui.request.TableColumnFiltering;
import com.flexicore.ui.request.TableColumnUpdate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.wizzdi.flexicore.security.service.BaseclassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;


@Extension
@Component
public class TableColumnService implements Plugin {

	private static final Logger logger=LoggerFactory.getLogger(TableColumnService.class);

	
	@Autowired
	private TableColumnRepository tableColumnRepository;

	
	@Autowired
	private UiFieldService uiFieldService;

	public TableColumn TableColumnUpdate(TableColumnUpdate TableColumnUpdate,
			SecurityContextBase securityContext) {
		if (TableColumnUpdateNoMerge(TableColumnUpdate, TableColumnUpdate.getTableColumn())) {
			tableColumnRepository.merge(TableColumnUpdate.getTableColumn());
		}
		return TableColumnUpdate.getTableColumn();
	}

	public boolean TableColumnUpdateNoMerge(
			TableColumnCreate tableColumnCreate, TableColumn tableColumn) {
		boolean update = uiFieldService.updateUiFieldNoMerge(tableColumnCreate, tableColumn);

		if (tableColumnCreate.getSortable() != null && tableColumnCreate.getSortable() != tableColumn.isSortable()) {
			update = true;
			tableColumn.setSortable(tableColumnCreate.getSortable());
		}

		if (tableColumnCreate.getFilterable() != null && tableColumnCreate.getFilterable() != tableColumn.isFilterable()) {
			update = true;
			tableColumn.setFilterable(tableColumnCreate.getFilterable());
		}

		if (tableColumnCreate.getDefaultColumnWidth() != null && tableColumnCreate.getDefaultColumnWidth() != tableColumn.getDefaultColumnWidth()) {
			update = true;
			tableColumn.setDefaultColumnWidth(tableColumnCreate.getDefaultColumnWidth());
		}

		return update;
	}

	public List<TableColumn> listAllTableColumns(
			TableColumnFiltering tableColumnFiltering,
			SecurityContextBase securityContext) {
		return tableColumnRepository.listAllTableColumns(tableColumnFiltering,
				securityContext);
	}

	public TableColumn createTableColumn(TableColumnCreate createTableColumn,
			SecurityContextBase securityContext) {
		TableColumn tableColumn = createTableColumnNoMerge(createTableColumn,
				securityContext);
		tableColumnRepository.merge(tableColumn);
		return tableColumn;

	}

	public TableColumn createTableColumnNoMerge(
			TableColumnCreate createTableColumn, SecurityContextBase securityContext) {
		TableColumn tableColumn = new TableColumn();
		tableColumn.setId(UUID.randomUUID().toString());
		TableColumnUpdateNoMerge(createTableColumn, tableColumn);
		BaseclassService.createSecurityObjectNoMerge(tableColumn,securityContext);
		return tableColumn;
	}

	public PaginationResponse<TableColumn> getAllTableColumns(
			TableColumnFiltering tableColumnFiltering,
			SecurityContextBase securityContext) {
		List<TableColumn> list = listAllTableColumns(tableColumnFiltering, securityContext);
		long count = tableColumnRepository.countAllTableColumns(tableColumnFiltering, securityContext);
		return new PaginationResponse<>(list, tableColumnFiltering, count);
	}

	public void validate(TableColumnCreate createTableColumn, SecurityContextBase securityContext) {
		uiFieldService.validate(createTableColumn, securityContext);
	}

	public void validate(TableColumnFiltering tableColumnFiltering, SecurityContextBase securityContext) {
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


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return tableColumnRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return tableColumnRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return tableColumnRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return tableColumnRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return tableColumnRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tableColumnRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tableColumnRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		tableColumnRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		tableColumnRepository.massMerge(toMerge);
	}

	public void validateCreate(TableColumnCreate createTableColumn, SecurityContextBase securityContext) {
		uiFieldService.validateCreate(createTableColumn,securityContext);
	}
}
