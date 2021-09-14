package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;

import com.flexicore.ui.model.ConfigurationPreset_;
import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.ConfigurationPreset;
import com.flexicore.ui.request.ConfigurationPresetCreate;
import com.flexicore.ui.request.ConfigurationPresetFiltering;
import com.flexicore.ui.request.ConfigurationPresetUpdate;
import com.flexicore.ui.service.ConfigurationPresetService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;




import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RestController
@RequestMapping("plugins/ConfigurationPresets")

@Tag(name = "ConfigurationPresets")
@Extension
@Component
public class ConfigurationPresetController implements Plugin {

	
	@Autowired
	private ConfigurationPresetService service;

	@Operation(summary = "getAllConfigurationPresets", description = "returns all ConfigurationPresets")
	@PostMapping("getAllConfigurationPresets")
	public PaginationResponse<ConfigurationPreset> getAllConfigurationPresets(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			ConfigurationPresetFiltering configurationPresetFiltering,
			@RequestAttribute SecurityContextBase securityContext) {
		return service.getAllConfigurationPresets(configurationPresetFiltering, securityContext);

	}


	@Operation(summary = "updateConfigurationPreset", description = "Updates Dashbaord")
	@PutMapping("updateConfigurationPreset")
	public ConfigurationPreset updateConfigurationPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			ConfigurationPresetUpdate updateConfigurationPreset,
			@RequestAttribute SecurityContextBase securityContext) {
		ConfigurationPreset configurationPresetToClazz = updateConfigurationPreset.getId() != null ? service.getByIdOrNull(updateConfigurationPreset.getId(), ConfigurationPreset.class, ConfigurationPreset_.security, securityContext) : null;
		if (configurationPresetToClazz == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no ConfigurationPreset with id  " + updateConfigurationPreset.getId());
		}
		updateConfigurationPreset.setConfigurationPreset(configurationPresetToClazz);
		return service.updateConfigurationPreset(updateConfigurationPreset, securityContext);

	}


	@Operation(summary = "createConfigurationPreset", description = "Creates Ui Field ")
	@PostMapping("createConfigurationPreset")
	public ConfigurationPreset createConfigurationPreset(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody
			ConfigurationPresetCreate createConfigurationPreset,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(createConfigurationPreset, securityContext);
		return service.createConfigurationPreset(createConfigurationPreset,
				securityContext);

	}

}
