package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.ui.model.PresetToPreset_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.flexicore.security.SecurityContextBase;
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




/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RestController
@RequestMapping("plugins/PresetToPresets")
@Tag(name = "PresetToPresets", description = "PresetToPreset support free definition of grids using Dynamic Execution as source of data")
@Tag(name = "Presets")
@Extension
@Component
public class PresetToPresetController implements Plugin {

	
	@Autowired
	private PresetToPresetService service;

	

	@Operation(summary = "getAllPresetToPresets", description = "returns all PresetToPresets")
	@PostMapping("getAllPresetToPresets")
	public PaginationResponse<PresetToPreset> getAllPresetToPresets(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			PresetToPresetFiltering presetToPresetFiltering,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(presetToPresetFiltering,securityContext);
		return service.getAllPresetToPresets(presetToPresetFiltering, securityContext);

	}

	

	@Operation(summary = "updatePresetToPreset", description = "Updates Dashbaord")
	@PutMapping("updatePresetToPreset")
	public PresetToPreset updatePresetToPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			PresetToPresetUpdate updatePresetToPreset, @RequestAttribute SecurityContextBase securityContext) {
		PresetToPreset presetToPreset = updatePresetToPreset.getId() != null ? service.getByIdOrNull(
				updatePresetToPreset.getId(), PresetToPreset.class, PresetToPreset_.security, securityContext) : null;
		if (presetToPreset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no PresetToPreset with id  " + updatePresetToPreset.getId());
		}
		updatePresetToPreset.setPresetToPreset(presetToPreset);
		service.validate(updatePresetToPreset, securityContext);

		return service.updatePresetToPreset(updatePresetToPreset, securityContext);

	}

	

	@Operation(summary = "createPresetToPreset", description = "Creates PresetToPreset ")
	@PostMapping("createPresetToPreset")
	public PresetToPreset createPresetToPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			PresetToPresetCreate createPresetToPreset, @RequestAttribute SecurityContextBase securityContext) {
		service.validate(createPresetToPreset, securityContext);
		return service.createPresetToPreset(createPresetToPreset, securityContext);

	}


}
