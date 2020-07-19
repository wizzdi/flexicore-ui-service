package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.FilterProperties;
import com.flexicore.ui.request.FilterPropertiesCreate;
import com.flexicore.ui.request.FilterPropertiesFiltering;
import com.flexicore.ui.request.FilterPropertiesUpdate;
import com.flexicore.ui.service.FilterPropertiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/FilterProperties")
@Tag(name = "FilterProperties")
@Tag(name = "Presets")
@Extension
@Component
public class FilterPropertiesRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private FilterPropertiesService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllFilterProperties", description = "returns all FilterProperties")
	@Path("getAllFilterProperties")
	public PaginationResponse<FilterProperties> getAllFilterProperties(
			@HeaderParam("authenticationKey") String authenticationKey,
			FilterPropertiesFiltering filterPropertiesFiltering,
			@Context SecurityContext securityContext) {
		return service.getAllFilterProperties(filterPropertiesFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateFilterProperties", description = "Updates Dashbaord")
	@Path("updateFilterProperties")
	public FilterProperties updateFilterProperties(
			@HeaderParam("authenticationKey") String authenticationKey,
			FilterPropertiesUpdate updateFilterProperties, @Context SecurityContext securityContext) {
		FilterProperties filterProperties = updateFilterProperties.getId() != null ? service.getByIdOrNull(
				updateFilterProperties.getId(), FilterProperties.class, null, securityContext) : null;
		if (filterProperties == null) {
			throw new BadRequestException("no ui field with id  "
					+ updateFilterProperties.getId());
		}
		updateFilterProperties.setFilterProperties(filterProperties);
		service.validate(updateFilterProperties, securityContext);

		return service.updateFilterProperties(updateFilterProperties, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createFilterProperties", description = "Creates FilterProperties ")
	@Path("createFilterProperties")
	public FilterProperties createFilterProperties(
			@HeaderParam("authenticationKey") String authenticationKey,
			FilterPropertiesCreate createFilterProperties, @Context SecurityContext securityContext) {
		service.validate(createFilterProperties, securityContext);
		return service.createFilterProperties(createFilterProperties, securityContext);

	}


}
