package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.model.FormField;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.FormFieldCreate;
import com.flexicore.ui.request.FormFieldFiltering;
import com.flexicore.ui.request.FormFieldUpdate;
import com.flexicore.ui.service.FormFieldService;
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
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/FormFields")
@OpenAPIDefinition(tags = {
        @Tag(name = "FormFields", description = "FormFields Services")
})
@Tag(name = "FormFields")

public class FormFieldRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private FormFieldService service;

    @Inject
    private CategoryService categoryService;


    @POST
    @Produces("application/json")
    @Operation(summary = "listAllFormFields", description = "List all Ui Fields")
    @Path("listAllFormFields")
    public PaginationResponse<FormField> getAllFormFields(
            @HeaderParam("authenticationKey") String authenticationKey,
            FormFieldFiltering formFieldFiltering, @Context SecurityContext securityContext) {
        service.validate(formFieldFiltering,securityContext);
        return service.getAllFormFields(formFieldFiltering, securityContext);

    }


    @POST
    @Produces("application/json")
    @Operation(summary = "updateFormField", description = "Updates Ui Field")
    @Path("updateFormField")
    public FormField updateFormField(
            @HeaderParam("authenticationKey") String authenticationKey,
            FormFieldUpdate formFieldUpdate, @Context SecurityContext securityContext) {
        FormField formField = formFieldUpdate.getId() != null ? service.getByIdOrNull(formFieldUpdate.getId(), FormField.class, null, securityContext) : null;
        if (formField == null) {
            throw new BadRequestException("no ui field with id  " + formFieldUpdate.getId());
        }
        formFieldUpdate.setFormField(formField);

        return service.FormFieldUpdate(formFieldUpdate, securityContext);

    }



    @POST
    @Produces("application/json")
    @Operation(summary = "createFormField", description = "Creates Ui Field ")
    @Path("createFormField")
    public FormField createFormField(
            @HeaderParam("authenticationKey") String authenticationKey,
            FormFieldCreate createFormField, @Context SecurityContext securityContext) {
        GridPreset preset = createFormField.getPresetId() != null ? service.getByIdOrNull(createFormField.getPresetId(), GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no GridPreset with id " + createFormField.getPresetId());
        }
        createFormField.setPreset(preset);
        service.validate(createFormField, securityContext);
        return service.createFormField(createFormField, securityContext);

    }








}

