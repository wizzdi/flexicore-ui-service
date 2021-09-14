package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.ui.model.MapPreset_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.flexicore.security.SecurityContextBase;
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




/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RestController
@RequestMapping("plugins/MapPresets")

@Tag(name = "MapPresets")
@Extension
@Component
public class MapPresetController implements Plugin {

	
	@Autowired
	private MapPresetService service;

	

	@Operation(summary = "getAllMapPresets", description = "returns all MapPresets")
	@PostMapping("getAllMapPresets")
	public PaginationResponse<MapPreset> getAllMapPresets(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			MapPresetFiltering mapPresetFiltering,
			@RequestAttribute SecurityContextBase securityContext) {
		return service.getAllMapPresets(mapPresetFiltering, securityContext);

	}

	

	@Operation(summary = "updateMapPreset", description = "Updates Dashbaord")
	@PutMapping("updateMapPreset")
	public MapPreset updateMapPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			MapPresetUpdate updateMapPreset,
			@RequestAttribute SecurityContextBase securityContext) {
		MapPreset mapPreset = updateMapPreset.getId() != null ? service.getByIdOrNull(updateMapPreset.getId(), MapPreset.class, MapPreset_.security, securityContext) : null;
		if (mapPreset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Map Preset with id  " + updateMapPreset.getId());
		}
		updateMapPreset.setMapPreset(mapPreset);
		return service.updateMapPreset(updateMapPreset,
				securityContext);

	}

	

	@Operation(summary = "createMapPreset", description = "Creates Ui Field ")
	@PostMapping("createMapPreset")
	public MapPreset createMapPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			MapPresetCreate createMapPreset,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(createMapPreset, securityContext);
		return service.createMapPreset(createMapPreset, securityContext);

	}

}
