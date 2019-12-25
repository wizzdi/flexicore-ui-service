package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.model.DashboardExecution;
import com.flexicore.ui.request.CreateDashboardExecution;
import com.flexicore.ui.request.DashboardExecutionFiltering;
import com.flexicore.ui.request.UpdateDashboardExecution;
import com.flexicore.ui.service.DashboardExecutionService;
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
@Path("plugins/DashboardExecutions")
@Tag(name = "DashboardExecutions")

public class DashboardExecutionRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private DashboardExecutionService service;


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllDashboardExecutions", description="returns all DashboardExecutions")
    @Path("getAllDashboardExecutions")
    public PaginationResponse<DashboardExecution> getAllDashboardExecutions(
            @HeaderParam("authenticationKey") String authenticationKey,
            DashboardExecutionFiltering dashboardExecutionFiltering, @Context SecurityContext securityContext) {
        return service.getAllDashboardExecutions(dashboardExecutionFiltering, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updateDashboardExecution", description="Updates Dashbaord")
    @Path("updateDashboardExecution")
    public DashboardExecution updateDashboardExecution(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateDashboardExecution updateDashboardExecution, @Context SecurityContext securityContext) {
        DashboardExecution dashboardExecutionToClazz = updateDashboardExecution.getId() != null ? service.getByIdOrNull(updateDashboardExecution.getId(), DashboardExecution.class, null, securityContext) : null;
        if (dashboardExecutionToClazz == null) {
            throw new BadRequestException("no ui field with id  " + updateDashboardExecution.getId());
        }
        updateDashboardExecution.setDashboardExecution(dashboardExecutionToClazz);

        return service.updateDashboardExecution(updateDashboardExecution, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createDashboardExecution", description="Creates Ui Field ")
    @Path("createDashboardExecution")
    public DashboardExecution createDashboardExecution(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateDashboardExecution createDashboardExecution, @Context SecurityContext securityContext) {
        service.validate(createDashboardExecution, securityContext);
        return service.createDashboardExecution(createDashboardExecution, securityContext);

    }


}

