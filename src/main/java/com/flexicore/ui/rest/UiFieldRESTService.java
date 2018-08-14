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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

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
@SwaggerDefinition(tags = {
        @Tag(name = "UiFields", description = "UiFields Services")
})
@Api(tags = {"UiFields"})

public class UiFieldRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private UiFieldService service;

    @Inject
    private CategoryService categoryService;


    @POST
    @Produces("application/json")
    @ApiOperation(value = "listAllUiFields", notes = "List all Ui Fields")
    @Path("listAllUiFields")
    public List<UiField> listAllUiFields(
            @HeaderParam("authenticationKey") String authenticationKey,
            UiFieldFiltering uiFieldFiltering, @Context SecurityContext securityContext) {
        return service.listAllUiFields(uiFieldFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @ApiOperation(value = "updateUiField", notes = "Updates Ui Field")
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
    @ApiOperation(value = "createUiField", notes = "Creates Ui Field ")
    @Path("createUiField")
    public UiField createUiField(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreateUiField createUiField, @Context SecurityContext securityContext) {
        Preset preset = createUiField.getPresetId() != null ? service.getByIdOrNull(createUiField.getPresetId(), Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + createUiField.getPresetId());
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
    @ApiOperation(value = "createPreset", notes = "Creates Preset ")
    @Path("createPreset")
    public Preset createPreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            CreatePreset createPreset, @Context SecurityContext securityContext) {
        validateCreatePreset(createPreset, securityContext);
        return service.createPreset(createPreset, securityContext);

    }

    private void validateCreatePreset(CreatePreset createPreset, SecurityContext securityContext) {
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
    @ApiOperation(value = "updatePreset", notes = "Updates Preset ")
    @Path("updatePreset")
    public Preset updatePreset(
            @HeaderParam("authenticationKey") String authenticationKey,
            UpdatePreset updatePreset, @Context SecurityContext securityContext) {
        Preset preset = updatePreset.getId() != null ? service.getByIdOrNull(updatePreset.getId(), Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + updatePreset.getId());
        }
        updatePreset.setPreset(preset);
        return service.updatePreset(updatePreset, securityContext);

    }


    @POST
    @Produces("application/json")
    @ApiOperation(value = "linkPresetToUser", notes = "Links preset to user")
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
    @ApiOperation(value = "linkPresetToRole", notes = "Links preset to Role")
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
    @ApiOperation(value = "linkPresetToTenant", notes = "Links preset to Tenant")
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
    @ApiOperation(value = "updatePresetToTenant", notes = "updates preset to Tenant")
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
    @ApiOperation(value = "updatePresetToUser", notes = "updates preset to User")
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
    @ApiOperation(value = "updatePresetToRole", notes = "updates preset to Role")
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

