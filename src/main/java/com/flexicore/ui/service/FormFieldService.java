package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.service.CategoryService;
import com.flexicore.ui.data.FormFieldRepository;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;
import com.flexicore.ui.response.PresetToRoleContainer;
import com.flexicore.ui.response.PresetToTenantContainer;
import com.flexicore.ui.response.PresetToUserContainer;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)

public class FormFieldService implements ServicePlugin {

    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private FormFieldRepository formFieldRepository;

    @Inject
    @PluginInfo(version = 1)
    private UiFieldService uiFieldService;


    public FormField FormFieldUpdate(FormFieldUpdate FormFieldUpdate, SecurityContext securityContext) {
        if (FormFieldUpdateNoMerge(FormFieldUpdate, FormFieldUpdate.getFormField())) {
            formFieldRepository.merge(FormFieldUpdate.getFormField());
        }
        return FormFieldUpdate.getFormField();
    }

    public boolean FormFieldUpdateNoMerge(FormFieldCreate formFieldCreate, FormField formField) {
        boolean update = uiFieldService.updateUiFieldNoMerge(formFieldCreate,formField);
        if (formFieldCreate.getEditable() != null && formFieldCreate.getEditable() != formField.isEditable()) {
            formField.setEditable(formFieldCreate.getEditable());
            update = true;
        }
        if (formFieldCreate.getCreatable() != null && formFieldCreate.getCreatable() != formField.isCreatable()) {
            formField.setCreatable(formFieldCreate.getCreatable());
            update = true;
        }
        if (formFieldCreate.getAnchorBottom() != null && formFieldCreate.getAnchorBottom() != formField.getAnchorBottom()) {
            formField.setAnchorBottom(formFieldCreate.getAnchorBottom());
            update = true;
        }

        if (formFieldCreate.getAnchorLeft() != null && formFieldCreate.getAnchorLeft() != formField.getAnchorLeft()) {
            formField.setAnchorLeft(formFieldCreate.getAnchorLeft());
            update = true;
        }
        if (formFieldCreate.getAnchorRight() != null && formFieldCreate.getAnchorRight() != formField.getAnchorRight()) {
            formField.setAnchorRight(formFieldCreate.getAnchorRight());
            update = true;
        }

        if (formFieldCreate.getAnchorTop() != null && formFieldCreate.getAnchorTop() != formField.getAnchorTop()) {
            formField.setAnchorTop(formFieldCreate.getAnchorTop());
            update = true;
        }
        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return formFieldRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public List<FormField> listAllFormFields(FormFieldFiltering formFieldFiltering, SecurityContext securityContext) {
        return formFieldRepository.listAllFormFields(formFieldFiltering,securityContext);
    }


    public FormField createFormField(FormFieldCreate createFormField, SecurityContext securityContext) {
        FormField formField = createFormFieldNoMerge(createFormField, securityContext);
        formFieldRepository.merge(formField);
        return formField;

    }

    public FormField createFormFieldNoMerge(FormFieldCreate createFormField, SecurityContext securityContext) {
        FormField formField = new FormField(createFormField.getName(), securityContext);
        FormFieldUpdateNoMerge(createFormField, formField);
        return formField;
    }



    public PaginationResponse<FormField> getAllFormFields(FormFieldFiltering formFieldFiltering, SecurityContext securityContext) {
        List<FormField> list=listAllFormFields(formFieldFiltering,securityContext);
        long count=formFieldRepository.countAllFormFields(formFieldFiltering,securityContext);
        return new PaginationResponse<>(list,formFieldFiltering,count);
    }



    public void validate(FormFieldCreate createFormField, SecurityContext securityContext) {
        uiFieldService.validate(createFormField,securityContext);
    }


    public void validate(FormFieldFiltering formFieldFiltering, SecurityContext securityContext) {
       uiFieldService.validate(formFieldFiltering,securityContext);
    }

    public FormFieldCreate getFormFieldCreate(FormField formField) {
        return new FormFieldCreate()
                .setEditable(formField.isEditable())
                .setCreatable(formField.isCreatable())
                .setAnchorBottom(formField.getAnchorBottom())
                .setAnchorLeft(formField.getAnchorLeft())
                .setAnchorRight(formField.getAnchorRight())
                .setAnchorTop(formField.getAnchorTop())
                .setPreset(formField.getPreset())
                .setCategory(formField.getCategory())
                .setDisplayName(formField.getDisplayName())
                .setDescription(formField.getDescription())
                .setVisible(formField.isVisible())
                .setPriority(formField.getPriority())
                .setName(formField.getName());
    }
}
