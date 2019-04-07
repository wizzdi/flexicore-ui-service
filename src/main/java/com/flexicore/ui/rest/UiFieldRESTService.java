package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.model.Category;
import com.flexicore.model.Role;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.container.request.*;
import com.flexicore.ui.model.*;
import com.flexicore.ui.service.UiFieldService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Operation(summary = "listAllUiFields", description="List all Ui Fields")
    @Path("listAllUiFields")
    public List<UiField> listAllUiFields(
            @HeaderParam("authenticationKey") String authenticationKey,
            UiFieldFiltering uiFieldFiltering, @Context SecurityContext securityContext) {
        return service.listAllUiFields(uiFieldFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "updateUiField", description="Updates Ui Field")
    @Path("updateUiField")
    public UiField updateUiField(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateUiField updateUiField, @Context SecurityContext securityContext) {
        UiField uiFieldToClazz = updateUiField.getId() != null ? service.getByIdOrNull(updateUiField.getId(), UiField.class, null, securityContext) : null;
        if (uiFieldToClazz == null) {
            throw new BadRequestException("no ui field with id  " + updateUiField.getId());
        }
        updateUiField.setUiField(uiFieldToClazz);

        return service.updateUiField(updateUiField, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createUiField", description="Creates Ui Field ")
    @Path("createUiField")
    public UiField createUiField(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateUiField createUiField, @Context SecurityContext securityContext) {
        GridPreset preset = createUiField.getPresetId() != null ? service.getByIdOrNull(createUiField.getPresetId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + createUiField.getPresetId());
        }
        createUiField.setPreset(preset);
        validateCreateUiField(createUiField, securityContext);
        return service.createUiField(createUiField, securityContext);

    }

    private void validateCreateUiField(CreateUiField createUiField, @Context SecurityContext securityContext) {
        Category category = categoryService.createCategory(createUiField.getCategoryName(), true, securityContext);
        createUiField.setCategory(category);
    }


    @POST
    @Produces("application/json")
    @Operation(summary = "createPreset", description="Creates Preset ")
    @Path("createPreset")
    public GridPreset createPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateGridPreset createPreset, @Context SecurityContext securityContext) {
        validateCreatePreset(createPreset, securityContext);
        return service.createGridPreset(createPreset, securityContext);

    }

    private void validateCreatePreset(CreateGridPreset createPreset, SecurityContext securityContext) {
        Map<String, List<CreateUiField>> map = createPreset.getUiFields().parallelStream().filter(f -> f.getCategoryName() != null).collect(Collectors.groupingBy(f -> f.getCategoryName(), Collectors.toList()));
        Map<String, Category> categoryMap = categoryService.getCategoriesByNames(map.keySet(), securityContext).parallelStream().collect(Collectors.toMap(f -> f.getName(), f -> f, (a, b) -> a));
        for (Map.Entry<String, List<CreateUiField>> entry : map.entrySet()) {
            Category category = categoryMap.computeIfAbsent(entry.getKey(), f -> categoryService.createCategory(f, false, securityContext));
            for (CreateUiField createUiField : entry.getValue()) {
                createUiField.setCategory(category);
            }
        }
    }

    @POST
    @Produces("application/json")
    @Operation(summary = "updatePreset", description="Updates Preset ")
    @Path("updatePreset")
    public GridPreset updatePreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateGridPreset updatePreset, @Context SecurityContext securityContext) {
        GridPreset preset = updatePreset.getId() != null ? service.getByIdOrNull(updatePreset.getId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + updatePreset.getId());
        }
        updatePreset.setPreset(preset);
        return service.updatePreset(updatePreset, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToUser", description="Links preset to user")
    @Path("linkPresetToUser")
    public PresetToUser linkPresetToUser(
            @HeaderParam("authenticationKey") String authenticationKey,
            LinkPresetToUser linkPresetToUser, @Context SecurityContext securityContext) {
        Preset preset = linkPresetToUser.getPresetId() != null ? service.getByIdOrNull(linkPresetToUser.getPresetId(), Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + linkPresetToUser.getPresetId());
        }
        linkPresetToUser.setPreset(preset);
        User user = linkPresetToUser.getUserId() != null ? service.getByIdOrNull(linkPresetToUser.getUserId(), User.class, null, securityContext) : null;
        if (user == null) {
            throw new BadRequestException("no user with id " + linkPresetToUser.getUserId());
        }
        linkPresetToUser.setUser(user);
        return service.linkPresetToUser(linkPresetToUser, securityContext);

    }

    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToRole", description="Links preset to Role")
    @Path("linkPresetToRole")
    public PresetToRole linkPresetToRole(
            @HeaderParam("authenticationKey") String authenticationKey,
            LinkPresetToRole linkPresetToRole, @Context SecurityContext securityContext) {
        Preset preset = linkPresetToRole.getPresetId() != null ? service.getByIdOrNull(linkPresetToRole.getPresetId(), Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + linkPresetToRole.getPresetId());
        }
        linkPresetToRole.setPreset(preset);
        Role role = linkPresetToRole.getRoleId() != null ? service.getByIdOrNull(linkPresetToRole.getRoleId(), Role.class, null, securityContext) : null;
        if (role == null) {
            throw new BadRequestException("no role with id " + linkPresetToRole.getRoleId());
        }
        linkPresetToRole.setRole(role);
        return service.linkPresetToRole(linkPresetToRole, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "linkPresetToTenant", description="Links preset to Tenant")
    @Path("linkPresetToTenant")
    public PresetToTenant linkPresetToTenant(
            @HeaderParam("authenticationKey") String authenticationKey,
            LinkPresetToTenant linkPresetToRole, @Context SecurityContext securityContext) {
        Preset preset = linkPresetToRole.getPresetId() != null ? service.getByIdOrNull(linkPresetToRole.getPresetId(), Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + linkPresetToRole.getPresetId());
        }
        linkPresetToRole.setPreset(preset);
        Tenant tenant = linkPresetToRole.getTenantId() != null ? service.getByIdOrNull(linkPresetToRole.getTenantId(), Tenant.class, null, securityContext) : null;
        if (tenant == null) {
            throw new BadRequestException("no tenant with id " + linkPresetToRole.getTenantId());
        }
        linkPresetToRole.setTenant(tenant);
        return service.linkPresetToTenant(linkPresetToRole, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToTenant", description="updates preset to Tenant")
    @Path("updatePresetToTenant")
    public PresetToTenant updatePresetToTenant(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateLinkPresetToTenant updateLinkPresetToTenant, @Context SecurityContext securityContext) {
        PresetToTenant preset = updateLinkPresetToTenant.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(), PresetToTenant.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToTenant(preset);
        return service.updatePresetToTenant(updateLinkPresetToTenant, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToUser", description="updates preset to User")
    @Path("updatePresetToUser")
    public PresetToUser updatePresetToUser(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateLinkPresetToUser updateLinkPresetToUser, @Context SecurityContext securityContext) {
        PresetToUser preset = updateLinkPresetToUser.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToUser.getLinkId(), PresetToUser.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToUser.getLinkId());
        }
        updateLinkPresetToUser.setPresetToUser(preset);
        return service.updatePresetToUser(updateLinkPresetToUser, securityContext);

    }


    @PUT
    @Produces("application/json")
    @Operation(summary = "updatePresetToRole", description="updates preset to Role")
    @Path("updatePresetToRole")
    public PresetToRole updatePresetToRole(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdateLinkPresetToRole updateLinkPresetToTenant, @Context SecurityContext securityContext) {
        PresetToRole preset = updateLinkPresetToTenant.getLinkId() != null ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(), PresetToRole.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no link with id " + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToRole(preset);
        return service.updatePresetToRole(updateLinkPresetToTenant, securityContext);

    }


}

