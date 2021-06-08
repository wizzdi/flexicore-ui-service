package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;
import com.flexicore.ui.response.PresetToRoleContainer;
import com.flexicore.ui.response.PresetToTenantContainer;
import com.flexicore.ui.response.PresetToUserContainer;
import com.flexicore.ui.service.UiFieldService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Created by Asaf on 04/06/2017.
 */


@OperationsInside
@RestController
@RequestMapping("plugins/UiFields")

@Tag(name = "UiFields")
@Extension
@Component
public class UiFieldController implements Plugin {


    @Autowired
    private UiFieldService service;


    @Operation(summary = "listAllUiFields", description = "List all Ui Fields")
    @PostMapping("getAllUiFields")
    public PaginationResponse<UiField> getAllUiFields(
            @RequestHeader("authenticationKey") String authenticationKey,
            @RequestBody UiFieldFiltering uiFieldFiltering,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(uiFieldFiltering, securityContext);
        return service.getAllUiFields(uiFieldFiltering, securityContext);

    }


    @Operation(summary = "linkPresetToUser", description = "Links preset to securityUser")
    @RequestMapping("linkPresetToUser")
    public PresetToUser linkPresetToUser(
            @RequestHeader("authenticationKey") String authenticationKey,
            @RequestBody PresetToUserCreate linkPresetToUser,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(linkPresetToUser, securityContext);
        return service.createPresetToUser(linkPresetToUser, securityContext);

    }


    @Operation(summary = "getAllPresetToRole", description = "getAllPresetToRole")
    @PostMapping("getAllPresetToRole")
    public PaginationResponse<PresetToRoleContainer> getAllPresetToRole(
            @RequestHeader("authenticationKey") String authenticationKey,
            @RequestBody PresetToRoleFilter presetToRoleFilter,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(presetToRoleFilter, securityContext);
        return service.getAllPresetToRole(presetToRoleFilter, securityContext);

    }


    @Operation(summary = "getAllPresetToUser", description = "getAllPresetToUser")
    @PostMapping("getAllPresetToUser")
    public PaginationResponse<PresetToUserContainer> getAllPresetToUser(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToUserFilter presetToUserFilter,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(presetToUserFilter, securityContext);
        return service.getAllPresetToUser(presetToUserFilter, securityContext);

    }


    @Operation(summary = "getAllPresetToTenant", description = "getAllPresetToTenant")
    @PostMapping("getAllPresetToTenant")
    public PaginationResponse<PresetToTenantContainer> getAllPresetToTenant(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToTenantFilter presetToTenantFilter,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(presetToTenantFilter, securityContext);
        return service.getAllPresetToTenant(presetToTenantFilter,
                securityContext);

    }


    @Operation(summary = "getPreferredPresets", description = "returns preferred presets")
    @RequestMapping("getPreferredPresets")
    public List<Preset> getPreferredPresets(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PreferedPresetRequest linkPresetToRole,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(linkPresetToRole, securityContext);
        return service.getPreferredPresets(linkPresetToRole, securityContext);

    }


    @Operation(summary = "linkPresetToRole", description = "Links preset to Role")
    @RequestMapping("linkPresetToRole")
    public PresetToRole linkPresetToRole(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToRoleCreate linkPresetToRole,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(linkPresetToRole, securityContext);
        return service.createPresetToRole(linkPresetToRole, securityContext);

    }


    @Operation(summary = "linkPresetToTenant", description = "Links preset to SecurityTenant")
    @RequestMapping("linkPresetToTenant")
    public PresetToTenant linkPresetToTenant(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToTenantCreate linkPresetToRole,
            @RequestAttribute SecurityContextBase securityContext) {
        service.validate(linkPresetToRole, securityContext);
        return service.createPresetToTenant(linkPresetToRole, securityContext);

    }


    @Operation(summary = "updatePresetToTenant", description = "updates preset to SecurityTenant")
    @PutMapping("updatePresetToTenant")
    public PresetToTenant updatePresetToTenant(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToTenantUpdate updateLinkPresetToTenant,
            @RequestAttribute SecurityContextBase securityContext) {
        PresetToTenant preset = updateLinkPresetToTenant.getLinkId() != null
                ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(),
                PresetToTenant.class, PresetToTenant_.security, securityContext) : null;
        if (preset == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no link with id "
                    + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToTenant(preset);
        return service.updatePresetToTenant(updateLinkPresetToTenant,
                securityContext);

    }


    @Operation(summary = "updatePresetToUser", description = "updates preset to SecurityUser")
    @PutMapping("updatePresetToUser")
    public PresetToUser updatePresetToUser(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToUserUpdate updateLinkPresetToUser,
            @RequestAttribute SecurityContextBase securityContext) {
        PresetToUser preset = updateLinkPresetToUser.getLinkId() != null
                ? service.getByIdOrNull(updateLinkPresetToUser.getLinkId(),
                PresetToUser.class, PresetToUser_.security, securityContext) : null;
        if (preset == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no link with id "
                    + updateLinkPresetToUser.getLinkId());
        }
        updateLinkPresetToUser.setPresetToUser(preset);
        return service.updatePresetToUser(updateLinkPresetToUser,
                securityContext);

    }


    @Operation(summary = "updatePresetToRole", description = "updates preset to Role")
    @PutMapping("updatePresetToRole")
    public PresetToRole updatePresetToRole(
            @RequestHeader("authenticationKey") String authenticationKey, @RequestBody
            PresetToRoleUpdate updateLinkPresetToTenant,
            @RequestAttribute SecurityContextBase securityContext) {
        PresetToRole preset = updateLinkPresetToTenant.getLinkId() != null
                ? service.getByIdOrNull(updateLinkPresetToTenant.getLinkId(),
                PresetToRole.class, PresetToRole_.security, securityContext) : null;
        if (preset == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no link with id "
                    + updateLinkPresetToTenant.getLinkId());
        }
        updateLinkPresetToTenant.setPresetToRole(preset);
        return service.updatePresetToRole(updateLinkPresetToTenant,
                securityContext);

    }

}
