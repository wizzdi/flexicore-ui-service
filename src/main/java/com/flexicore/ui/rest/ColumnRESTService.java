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
import com.flexicore.ui.service.ColumnService;
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
@Path("plugins/Columns")
@OpenAPIDefinition(tags = {
        @Tag(name = "Columns", description = "Columns Services")
})
@Tag(name = "Columns")

public class ColumnRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ColumnService service;

    @Inject
    private CategoryService categoryService;


    @POST
    @Produces("application/json")
    @Operation(summary = "listAllColumns", description = "List all Ui Fields")
    @Path("listAllColumns")
    public PaginationResponse<Column> getAllColumns(
            @HeaderParam("authenticationKey") String authenticationKey,
            ColumnFiltering columnFiltering, @Context SecurityContext securityContext) {
        service.validate(columnFiltering,securityContext);
        return service.getAllColumns(columnFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "updateColumn", description = "Updates Ui Field")
    @Path("updateColumn")
    public Column updateColumn(
            @HeaderParam("authenticationKey") String authenticationKey,
            ColumnUpdate columnUpdate, @Context SecurityContext securityContext) {
        Column column = columnUpdate.getId() != null ? service.getByIdOrNull(columnUpdate.getId(), Column.class, null, securityContext) : null;
        if (column == null) {
            throw new BadRequestException("no ui field with id  " + columnUpdate.getId());
        }
        columnUpdate.setColumn(column);

        return service.ColumnUpdate(columnUpdate, securityContext);

    }



    @POST
    @Produces("application/json")
    @Operation(summary = "createColumn", description = "Creates Ui Field ")
    @Path("createColumn")
    public Column createColumn(
            @HeaderParam("authenticationKey") String authenticationKey,
            ColumnCreate createColumn, @Context SecurityContext securityContext) {
        GridPreset preset = createColumn.getPresetId() != null ? service.getByIdOrNull(createColumn.getPresetId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + createColumn.getPresetId());
        }
        createColumn.setPreset(preset);
        service.validate(createColumn, securityContext);
        return service.createColumn(createColumn, securityContext);

    }








}

