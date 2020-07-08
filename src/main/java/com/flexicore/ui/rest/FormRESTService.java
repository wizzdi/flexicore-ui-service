package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.request.FormCopy;
import com.flexicore.ui.request.FormCreate;
import com.flexicore.ui.request.FormFiltering;
import com.flexicore.ui.request.FormUpdate;
import com.flexicore.ui.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Asaf on 04/06/2017.
 */

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/Forms")
@Tag(name = "Forms", description = "Form support free definition of grids using Dynamic Execution as source of data")
@Tag(name = "Presets")
@Extension
@Component
public class FormRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private FormService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllForms", description = "returns all Forms")
	@Path("getAllForms")
	public PaginationResponse<Form> getAllForms(
			@HeaderParam("authenticationKey") String authenticationKey,
			FormFiltering formFiltering,
			@Context SecurityContext securityContext) {
		return service.getAllForms(formFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateForm", description = "Updates Dashbaord")
	@Path("updateForm")
	public Form updateForm(
			@HeaderParam("authenticationKey") String authenticationKey,
			FormUpdate updateForm, @Context SecurityContext securityContext) {
		Form form = updateForm.getId() != null ? service.getByIdOrNull(
				updateForm.getId(), Form.class, null, securityContext) : null;
		if (form == null) {
			throw new BadRequestException("no ui field with id  "
					+ updateForm.getId());
		}
		updateForm.setForm(form);
		service.validate(updateForm, securityContext);

		return service.updateForm(updateForm, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createForm", description = "Creates Form ")
	@Path("createForm")
	public Form createForm(
			@HeaderParam("authenticationKey") String authenticationKey,
			FormCreate createForm, @Context SecurityContext securityContext) {
		service.validate(createForm, securityContext);
		return service.createForm(createForm, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "copyForm", description = "Copies Form")
	@Path("copyForm")
	public Form copyForm(
			@HeaderParam("authenticationKey") String authenticationKey,
			FormCopy formCopy, @Context SecurityContext securityContext) {
		service.validate(formCopy, securityContext);
		return service.copyForm(formCopy, securityContext);

	}

}
