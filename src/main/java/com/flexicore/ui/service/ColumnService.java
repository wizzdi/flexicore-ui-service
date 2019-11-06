package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.data.ColumnRepository;
import com.flexicore.ui.model.Column;
import com.flexicore.ui.request.ColumnCreate;
import com.flexicore.ui.request.ColumnFiltering;
import com.flexicore.ui.request.ColumnUpdate;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)

public class ColumnService implements ServicePlugin {

    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private ColumnRepository columnRepository;


    @Inject
    @PluginInfo(version = 1)
    private UiFieldService uiFieldService;


    public Column ColumnUpdate(ColumnUpdate ColumnUpdate, SecurityContext securityContext) {
        if (ColumnUpdateNoMerge(ColumnUpdate, ColumnUpdate.getColumn())) {
            columnRepository.merge(ColumnUpdate.getColumn());
        }
        return ColumnUpdate.getColumn();
    }

    public boolean ColumnUpdateNoMerge(ColumnCreate columnCreate, Column column) {
        boolean update = uiFieldService.updateUiFieldNoMerge(columnCreate, column);


        if (columnCreate.getSortable() != null && columnCreate.getSortable() != column.isSortable()) {
            update = true;
            column.setSortable(columnCreate.getSortable());
        }

        if (columnCreate.getFilterable() != null && columnCreate.getFilterable() != column.isFilterable()) {
            update = true;
            column.setFilterable(columnCreate.getFilterable());
        }

        if (columnCreate.getDefaultColumnWidth() != null && columnCreate.getDefaultColumnWidth() != column.getDefaultColumnWidth()) {
            update = true;
            column.setDefaultColumnWidth(columnCreate.getDefaultColumnWidth());
        }

        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return columnRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public List<Column> listAllColumns(ColumnFiltering columnFiltering, SecurityContext securityContext) {
        return columnRepository.listAllColumns(columnFiltering, securityContext);
    }


    public Column createColumn(ColumnCreate createColumn, SecurityContext securityContext) {
        Column column = createColumnNoMerge(createColumn, securityContext);
        columnRepository.merge(column);
        return column;

    }

    public Column createColumnNoMerge(ColumnCreate createColumn, SecurityContext securityContext) {
        Column column = new Column(createColumn.getName(), securityContext);
        ColumnUpdateNoMerge(createColumn, column);
        return column;
    }


    public PaginationResponse<Column> getAllColumns(ColumnFiltering columnFiltering, SecurityContext securityContext) {
        List<Column> list = listAllColumns(columnFiltering, securityContext);
        long count = columnRepository.countAllColumns(columnFiltering, securityContext);
        return new PaginationResponse<>(list, columnFiltering, count);
    }


    public void validate(ColumnCreate createColumn, SecurityContext securityContext) {
        uiFieldService.validate(createColumn, securityContext);
    }


    public void validate(ColumnFiltering columnFiltering, SecurityContext securityContext) {
        uiFieldService.validate(columnFiltering, securityContext);
    }

    public ColumnCreate getColumnCreate(Column column) {
        return new ColumnCreate()
                .setFilterable(column.isFilterable())
                .setSortable(column.isSortable())
                .setDefaultColumnWidth(column.getDefaultColumnWidth())
                .setPreset(column.getPreset())
                .setCategory(column.getCategory())
                .setDisplayName(column.getDisplayName())
                .setDescription(column.getDescription())
                .setVisible(column.isVisible())
                .setPriority(column.getPriority())
                .setName(column.getName());
    }
}
