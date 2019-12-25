package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.ConfigurationPreset;
import com.flexicore.ui.request.CreateConfigurationPreset;
import com.flexicore.ui.request.ConfigurationPresetFiltering;
import com.flexicore.ui.request.UpdateConfigurationPreset;
import com.flexicore.ui.service.ConfigurationPresetService;
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
@Path("plugins/ConfigurationPresets")
@OpenAPIDefinition(tags = {
        @Tag(name = "ConfigurationPresets", description = "ConfigurationPresets Services")
})
@Tag(name = "ConfigurationPresets")

public class ConfigurationPresetRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ConfigurationPresetService service;


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllConfigurationPresets", description="returns all ConfigurationPresets")
    @Path("getAllConfigurationPresets")
    public PaginationResponse<ConfigurationPreset> getAllConfigurationPresets(
            @HeaderParam("authenticationKey") String authenticationKey,
            ConfigurationPresetFiltering configurationPresetFiltering, @Context SecurityContext securityContext) {
        return service.getAllConfigurationPresets(configurationPresetFiltering, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updateConfigurationPreset", description="Updates Dashbaord")
    @Path("updateConfigurationPreset")
    public ConfigurationPreset updateConfigurationPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateConfigurationPreset updateConfigurationPreset, @Context SecurityContext securityContext) {
        ConfigurationPreset configurationPresetToClazz = updateConfigurationPreset.getId() != null ? service.getByIdOrNull(updateConfigurationPreset.getId(), ConfigurationPreset.class, null, securityContext) : null;
        if (configurationPresetToClazz == null) {
            throw new BadRequestException("no ui field with id  " + updateConfigurationPreset.getId());
        }
        updateConfigurationPreset.setConfigurationPreset(configurationPresetToClazz);

        return service.updateConfigurationPreset(updateConfigurationPreset, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createConfigurationPreset", description="Creates Ui Field ")
    @Path("createConfigurationPreset")
    public ConfigurationPreset createConfigurationPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateConfigurationPreset createConfigurationPreset, @Context SecurityContext securityContext) {
        service.validate(createConfigurationPreset, securityContext);
        return service.createConfigurationPreset(createConfigurationPreset, securityContext);

    }


}

