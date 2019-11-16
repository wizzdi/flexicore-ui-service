package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;
import com.flexicore.ui.response.PresetToRoleContainer;
import com.flexicore.ui.response.PresetToTenantContainer;
import com.flexicore.ui.response.PresetToUserContainer;
import com.flexicore.ui.service.TableColumnService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by Asaf on 04/06/2017.
 */


@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/TableColumns")
@OpenAPIDefinition(tags = {
        @Tag(name = "TableColumns", description = "TableColumns Services")
})
@Tag(name = "TableColumns")

public class TableColumnRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private TableColumnService service;

    @Inject
    private CategoryService categoryService;


    @POST
    @Produces("application/json")
    @Operation(summary = "listAllTableColumns", description = "List all Ui Fields")
    @Path("listAllTableColumns")
    public PaginationResponse<TableColumn> getAllTableColumns(
            @HeaderParam("authenticationKey") String authenticationKey,
            TableColumnFiltering tableColumnFiltering, @Context SecurityContext securityContext) {
        service.validate(tableColumnFiltering,securityContext);
        return service.getAllTableColumns(tableColumnFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "updateTableColumn", description = "Updates Ui Field")
    @Path("updateTableColumn")
    public TableColumn updateTableColumn(
            @HeaderParam("authenticationKey") String authenticationKey,
            TableColumnUpdate tableColumnUpdate, @Context SecurityContext securityContext) {
        TableColumn tableColumn = tableColumnUpdate.getId() != null ? service.getByIdOrNull(tableColumnUpdate.getId(), TableColumn.class, null, securityContext) : null;
        if (tableColumn == null) {
            throw new BadRequestException("no ui field with id  " + tableColumnUpdate.getId());
        }
        tableColumnUpdate.setTableColumn(tableColumn);

        return service.TableColumnUpdate(tableColumnUpdate, securityContext);

    }



    @POST
    @Produces("application/json")
    @Operation(summary = "createTableColumn", description = "Creates Ui Field ")
    @Path("createTableColumn")
    public TableColumn createTableColumn(
            @HeaderParam("authenticationKey") String authenticationKey,
            TableColumnCreate createTableColumn, @Context SecurityContext securityContext) {
        GridPreset preset = createTableColumn.getPresetId() != null ? service.getByIdOrNull(createTableColumn.getPresetId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + createTableColumn.getPresetId());
        }
        createTableColumn.setPreset(preset);
        service.validate(createTableColumn, securityContext);
        return service.createTableColumn(createTableColumn, securityContext);

    }








}

