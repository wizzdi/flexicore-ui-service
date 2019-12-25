package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.model.DashboardElement;
import com.flexicore.ui.request.CreateDashboardElement;
import com.flexicore.ui.request.DashboardElementFiltering;
import com.flexicore.ui.request.UpdateDashboardElement;
import com.flexicore.ui.service.DashboardElementService;
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
@ProtectedREST
@Path("plugins/DashboardElements")
@Tag(name = "DashboardElements")

public class DashboardElementRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private DashboardElementService service;


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllDashboardElements", description="returns all DashboardElements")
    @Path("getAllDashboardElements")
    public PaginationResponse<DashboardElement> getAllDashboardElements(
            @HeaderParam("authenticationKey") String authenticationKey,
            DashboardElementFiltering dashboardElementFiltering, @Context SecurityContext securityContext) {
        return service.getAllDashboardElements(dashboardElementFiltering, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updateDashboardElement", description="Updates Dashbaord")
    @Path("updateDashboardElement")
    public DashboardElement updateDashboardElement(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateDashboardElement updateDashboardElement, @Context SecurityContext securityContext) {
        DashboardElement dashboardElementToClazz = updateDashboardElement.getId() != null ? service.getByIdOrNull(updateDashboardElement.getId(), DashboardElement.class, null, securityContext) : null;
        if (dashboardElementToClazz == null) {
            throw new BadRequestException("no ui field with id  " + updateDashboardElement.getId());
        }
        updateDashboardElement.setDashboardElement(dashboardElementToClazz);

        return service.updateDashboardElement(updateDashboardElement, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createDashboardElement", description="Creates Ui Field ")
    @Path("createDashboardElement")
    public DashboardElement createDashboardElement(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateDashboardElement createDashboardElement, @Context SecurityContext securityContext) {
        service.validate(createDashboardElement, securityContext);
        return service.createDashboardElement(createDashboardElement, securityContext);

    }


}

