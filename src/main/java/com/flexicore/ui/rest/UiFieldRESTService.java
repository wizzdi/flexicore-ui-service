package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;
import com.flexicore.ui.response.PresetToRoleContainer;
import com.flexicore.ui.response.PresetToTenantContainer;
import com.flexicore.ui.response.PresetToUserContainer;
import com.flexicore.ui.service.UiFieldService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Created by Asaf on 04/06/2017.
 */


@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/UiFields")
@OpenAPIDefinition(tags = {
        @Tag(name = "UiFields", description = "UiFields Services")
})
@Tag(name = "UiFields")

public class UiFieldRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private UiFieldService service;

    @Inject
    private CategoryService categoryService;


    @POST
    @Produces("application/json")
    @Operation(summary = "listAllUiFields", description = "List all Ui Fields")
    @Path("listAllUiFields")
    public PaginationResponse<UiField> getAllUiFields(
            @HeaderParam("authenticationKey") String authenticationKey,
            UiFieldFiltering uiFieldFiltering, @Context SecurityContext securityContext) {
        service.validate(uiFieldFiltering,securityContext);
        return service.getAllUiFields(uiFieldFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "updateUiField", description = "Updates Ui Field")
    @Path("updateUiField")
    public UiField updateUiField(
            @HeaderParam("authenticationKey") String authenticationKey,
            UiFieldUpdate updateUiField, @Context SecurityContext securityContext) {
        UiField uiField = updateUiField.getId() != null ? service.getByIdOrNull(updateUiField.getId(), UiField.class, null, securityContext) : null;
        if (uiField == null) {
            throw new BadRequestException("no ui field with id  " + updateUiField.getId());
        }
        updateUiField.setUiField(uiField);

        return service.updateUiField(updateUiField, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "massCreateUiFields", description = "Mass Creates Ui Field ")
    @Path("massCreateUiFields")
    public List<UiField> massCreateUiFields(
            @HeaderParam("authenticationKey") String authenticationKey,
            MassCreateUiFields massCreateUiFields, @Context SecurityContext securityContext) {
      service.validate(massCreateUiFields,securityContext);
        return service.massCreateUiFields(massCreateUiFields, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createUiField", description = "Creates Ui Field ")
    @Path("createUiField")
    public UiField createUiField(
            @HeaderParam("authenticationKey") String authenticationKey,
            UiFieldCreate createUiField, @Context SecurityContext securityContext) {
        GridPreset preset = createUiField.getPresetId() != null ? service.getByIdOrNull(createUiField.getPresetId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + createUiField.getPresetId());
        }
        createUiField.setPreset(preset);
        service.validate(createUiField, securityContext);
        return service.createUiField(createUiField, securityContext);

    }








    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToUser", description = "Links preset to user")
    @Path("linkPresetToUser")
    public PresetToUser linkPresetToUser(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToUserCreate linkPresetToUser, @Context SecurityContext securityContext) {
        service.validate(linkPresetToUser, securityContext);
        return service.createPresetToUser(linkPresetToUser, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllPresetToRole", description = "getAllPresetToRole")
    @Path("getAllPresetToRole")
    public PaginationResponse<PresetToRoleContainer> getAllPresetToRole(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToRoleFilter presetToRoleFilter, @Context SecurityContext securityContext) {
        service.validate(presetToRoleFilter, securityContext);
        return service.getAllPresetToRole(presetToRoleFilter, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllPresetToUser", description = "getAllPresetToUser")
    @Path("getAllPresetToUser")
    public PaginationResponse<PresetToUserContainer> getAllPresetToUser(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToUserFilter presetToUserFilter, @Context SecurityContext securityContext) {
        service.validate(presetToUserFilter, securityContext);
        return service.getAllPresetToUser(presetToUserFilter, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "getAllPresetToTenant", description = "getAllPresetToTenant")
    @Path("getAllPresetToTenant")
    public PaginationResponse<PresetToTenantContainer> getAllPresetToTenant(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToTenantFilter presetToTenantFilter, @Context SecurityContext securityContext) {
        service.validate(presetToTenantFilter, securityContext);
        return service.getAllPresetToTenant(presetToTenantFilter, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToRole", description = "Links preset to Role")
    @Path("linkPresetToRole")
    public PresetToRole linkPresetToRole(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToRoleCreate linkPresetToRole, @Context SecurityContext securityContext) {
        service.validate(linkPresetToRole, securityContext);
        return service.createPresetToRole(linkPresetToRole, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToTenant", description = "Links preset to Tenant")
    @Path("linkPresetToTenant")
    public PresetToTenant linkPresetToTenant(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToTenantCreate linkPresetToRole, @Context SecurityContext securityContext) {
        service.validate(linkPresetToRole, securityContext);
        return service.createPresetToTenant(linkPresetToRole, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToTenant", description = "updates preset to Tenant")
    @Path("updatePresetToTenant")
    public PresetToTenant updatePresetToTenant(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToTenantUpdate updateLinkPresetToTenant, @Context SecurityContext securityContext) {
        PresetToTenant preset = updateLinkPresetToTenant.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(), PresetToTenant.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToTenant(preset);
        return service.updatePresetToTenant(updateLinkPresetToTenant, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToUser", description = "updates preset to User")
    @Path("updatePresetToUser")
    public PresetToUser updatePresetToUser(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToUserUpdate updateLinkPresetToUser, @Context SecurityContext securityContext) {
        PresetToUser preset = updateLinkPresetToUser.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToUser.getLinkId(), PresetToUser.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToUser.getLinkId());
        }
        updateLinkPresetToUser.setPresetToUser(preset);
        return service.updatePresetToUser(updateLinkPresetToUser, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToRole", description = "updates preset to Role")
    @Path("updatePresetToRole")
    public PresetToRole updatePresetToRole(
            @HeaderParam("authenticationKey") String authenticationKey,
            PresetToRoleUpdate updateLinkPresetToTenant, @Context SecurityContext securityContext) {
        PresetToRole preset = updateLinkPresetToTenant.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(), PresetToRole.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToRole(preset);
        return service.updatePresetToRole(updateLinkPresetToTenant, securityContext);

    }


}

