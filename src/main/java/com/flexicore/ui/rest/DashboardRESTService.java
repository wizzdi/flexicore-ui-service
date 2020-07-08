package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Dashboard;
import com.flexicore.ui.request.CreateDashboard;
import com.flexicore.ui.request.DashboardFiltering;
import com.flexicore.ui.request.UpdateDashboard;
import com.flexicore.ui.service.DashboardService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/Dashboards")
@OpenAPIDefinition(tags = {@Tag(name = "Dashboards", description = "Dashboards Services")})
@Tag(name = "Dashboards")
@Extension
@Component
public class DashboardRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private DashboardService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllDashboards", description = "returns all Dashboards")
	@Path("getAllDashboards")
	public PaginationResponse<Dashboard> getAllDashboards(
			@HeaderParam("authenticationKey") String authenticationKey,
			DashboardFiltering dashboardFiltering,
			@Context SecurityContext securityContext) {
		return service.getAllDashboards(dashboardFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateDashboard", description = "Updates Dashbaord")
	@Path("updateDashboard")
	public Dashboard updateDashboard(
			@HeaderParam("authenticationKey") String authenticationKey,
			UpdateDashboard updateDashboard,
			@Context SecurityContext securityContext) {
		Dashboard dashboardToClazz = updateDashboard.getId() != null ? service
				.getByIdOrNull(updateDashboard.getId(), Dashboard.class, null,
						securityContext) : null;
		if (dashboardToClazz == null) {
			throw new BadRequestException("no ui field with id  "
					+ updateDashboard.getId());
		}
		updateDashboard.setDashboard(dashboardToClazz);

		return service.updateDashboard(updateDashboard, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createDashboard", description = "Creates Ui Field ")
	@Path("createDashboard")
	public Dashboard createDashboard(
			@HeaderParam("authenticationKey") String authenticationKey,
			CreateDashboard createDashboard,
			@Context SecurityContext securityContext) {
		service.validate(createDashboard, securityContext);
		return service.createDashboard(createDashboard, securityContext);

	}

}
