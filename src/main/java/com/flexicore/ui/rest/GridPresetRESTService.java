package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.container.request.GridPresetCreate;
import com.flexicore.ui.container.request.GridPresetUpdate;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.GridPresetFiltering;
import com.flexicore.ui.service.GridPresetService;
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
@Path("plugins/GridPresets")
@OpenAPIDefinition(tags = {
        @Tag(name = "GridPresets", description = "GridPresets Services")
})
@Tag(name = "GridPresets")

public class GridPresetRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private GridPresetService service;


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllGridPresets", description="returns all GridPresets")
    @Path("getAllGridPresets")
    public PaginationResponse<GridPreset> getAllGridPresets(
            @HeaderParam("authenticationKey") String authenticationKey,
            GridPresetFiltering gridPresetFiltering, @Context SecurityContext securityContext) {
        return service.getAllGridPresets(gridPresetFiltering, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updateGridPreset", description="Updates Dashbaord")
    @Path("updateGridPreset")
    public GridPreset updateGridPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            GridPresetUpdate updateGridPreset, @Context SecurityContext securityContext) {
        GridPreset gridPreset = updateGridPreset.getId() != null ? service.getByIdOrNull(updateGridPreset.getId(), GridPreset.class, null, securityContext) : null;
        if (gridPreset == null) {
            throw new BadRequestException("no ui field with id  " + updateGridPreset.getId());
        }
        updateGridPreset.setPreset(gridPreset);
        service.validate(updateGridPreset,securityContext);

        return service.updateGridPreset(updateGridPreset, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createGridPreset", description="Creates Ui Field ")
    @Path("createGridPreset")
    public GridPreset createGridPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            GridPresetCreate createGridPreset, @Context SecurityContext securityContext) {
        service.validate(createGridPreset, securityContext);
        return service.createGridPreset(createGridPreset, securityContext);

    }


}

