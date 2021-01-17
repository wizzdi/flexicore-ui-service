package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.PresetToPreset;
import com.flexicore.ui.request.PresetToPresetCreate;
import com.flexicore.ui.request.PresetToPresetFiltering;
import com.flexicore.ui.request.PresetToPresetUpdate;
import com.flexicore.ui.service.PresetToPresetService;
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
@Path("plugins/PresetToPresets")
@Tag(name = "PresetToPresets", description = "PresetToPreset support free definition of grids using Dynamic Execution as source of data")
@Tag(name = "Presets")
@Extension
@Component
public class PresetToPresetRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private PresetToPresetService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllPresetToPresets", description = "returns all PresetToPresets")
	@Path("getAllPresetToPresets")
	public PaginationResponse<PresetToPreset> getAllPresetToPresets(
			@HeaderParam("authenticationKey") String authenticationKey,
			PresetToPresetFiltering presetToPresetFiltering,
			@Context SecurityContext securityContext) {
		service.validate(presetToPresetFiltering,securityContext);
		return service.getAllPresetToPresets(presetToPresetFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updatePresetToPreset", description = "Updates Dashbaord")
	@Path("updatePresetToPreset")
	public PresetToPreset updatePresetToPreset(
			@HeaderParam("authenticationKey") String authenticationKey,
			PresetToPresetUpdate updatePresetToPreset, @Context SecurityContext securityContext) {
		PresetToPreset presetToPreset = updatePresetToPreset.getId() != null ? service.getByIdOrNull(
				updatePresetToPreset.getId(), PresetToPreset.class, null, securityContext) : null;
		if (presetToPreset == null) {
			throw new BadRequestException("no PresetToPreset with id  " + updatePresetToPreset.getId());
		}
		updatePresetToPreset.setPresetToPreset(presetToPreset);
		service.validate(updatePresetToPreset, securityContext);

		return service.updatePresetToPreset(updatePresetToPreset, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createPresetToPreset", description = "Creates PresetToPreset ")
	@Path("createPresetToPreset")
	public PresetToPreset createPresetToPreset(
			@HeaderParam("authenticationKey") String authenticationKey,
			PresetToPresetCreate createPresetToPreset, @Context SecurityContext securityContext) {
		service.validate(createPresetToPreset, securityContext);
		return service.createPresetToPreset(createPresetToPreset, securityContext);

	}


}
