package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.MapPreset;
import com.flexicore.ui.request.MapPresetCreate;
import com.flexicore.ui.request.MapPresetFiltering;
import com.flexicore.ui.request.MapPresetUpdate;
import com.flexicore.ui.service.MapPresetService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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
@Path("plugins/MapPresets")
@OpenAPIDefinition(tags = {@Tag(name = "MapPresets", description = "MapPresets Services")})
@Tag(name = "MapPresets")
@Extension
@Component
public class MapPresetRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private MapPresetService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllMapPresets", description = "returns all MapPresets")
	@Path("getAllMapPresets")
	public PaginationResponse<MapPreset> getAllMapPresets(
			@HeaderParam("authenticationKey") String authenticationKey,
			MapPresetFiltering mapPresetFiltering,
			@Context SecurityContext securityContext) {
		return service.getAllMapPresets(mapPresetFiltering,
				securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateMapPreset", description = "Updates Dashbaord")
	@Path("updateMapPreset")
	public MapPreset updateMapPreset(
			@HeaderParam("authenticationKey") String authenticationKey,
			MapPresetUpdate updateMapPreset,
			@Context SecurityContext securityContext) {
		MapPreset mapPresetToClazz = updateMapPreset
				.getId() != null ? service.getByIdOrNull(
				updateMapPreset.getId(), MapPreset.class,
				null, securityContext) : null;
		if (mapPresetToClazz == null) {
			throw new BadRequestException("no ui field with id  "
					+ updateMapPreset.getId());
		}
		updateMapPreset
				.setMapPreset(mapPresetToClazz);

		return service.updateMapPreset(updateMapPreset,
				securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createMapPreset", description = "Creates Ui Field ")
	@Path("createMapPreset")
	public MapPreset createMapPreset(
			@HeaderParam("authenticationKey") String authenticationKey,
			MapPresetCreate createMapPreset,
			@Context SecurityContext securityContext) {
		service.validate(createMapPreset, securityContext);
		return service.createMapPreset(createMapPreset,
				securityContext);

	}

}
