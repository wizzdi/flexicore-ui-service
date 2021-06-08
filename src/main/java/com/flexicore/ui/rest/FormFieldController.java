package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;

import com.flexicore.ui.model.FormField_;
import com.wizzdi.flexicore.security.response.PaginationResponse;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.FormField;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.FormFieldCreate;
import com.flexicore.ui.request.FormFieldFiltering;
import com.flexicore.ui.request.FormFieldUpdate;
import com.flexicore.ui.service.FormFieldService;
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
@RequestMapping("plugins/FormFields")

@Tag(name = "FormFields")
@Extension
@Component
public class FormFieldController implements Plugin {

	
	@Autowired
	private FormFieldService service;


	

	@Operation(summary = "getAllFormFields", description = "List all Ui Fields")
	@PostMapping("getAllFormFields")
	public PaginationResponse<FormField> getAllFormFields(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			FormFieldFiltering formFieldFiltering,
			@RequestAttribute SecurityContextBase securityContext) {
		service.validate(formFieldFiltering, securityContext);
		return service.getAllFormFields(formFieldFiltering, securityContext);

	}



	@Operation(summary = "updateFormField", description = "Updates Ui Field")
	@PutMapping("updateFormField")
	public FormField updateFormField(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			FormFieldUpdate formFieldUpdate,
			@RequestAttribute SecurityContextBase securityContext) {
		FormField formField = formFieldUpdate.getId() != null ? service.getByIdOrNull(formFieldUpdate.getId(), FormField.class, FormField_.security, securityContext) : null;
		if (formField == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no FormField with id  " + formFieldUpdate.getId());
		}
		formFieldUpdate.setFormField(formField);
		return service.FormFieldUpdate(formFieldUpdate, securityContext);

	}

	

	@Operation(summary = "createFormField", description = "Creates Ui Field ")
	@PostMapping("createFormField")
	public FormField createFormField(
			@RequestHeader("authenticationKey") String authenticationKey, @RequestBody 
			FormFieldCreate createFormField,
			@RequestAttribute SecurityContextBase securityContext) {
		GridPreset preset = createFormField.getPresetId() != null ? service
				.getByIdOrNull(createFormField.getPresetId(), GridPreset.class,
						null, securityContext) : null;
		if (preset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no GridPreset with id "
					+ createFormField.getPresetId());
		}
		createFormField.setPreset(preset);
		service.validate(createFormField, securityContext);
		return service.createFormField(createFormField, securityContext);

	}

}
