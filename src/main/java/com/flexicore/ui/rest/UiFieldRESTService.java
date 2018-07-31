package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.annotations.rest.Read;
import com.flexicore.annotations.rest.Write;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.model.Clazz;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.container.request.LinkUiFieldRequest;
import com.flexicore.ui.container.request.UiFieldFiltering;
import com.flexicore.ui.container.request.UpdateUiFieldLink;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.model.UiFieldToClazz;
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

/**
 * Created by Asaf on 04/06/2017.
 */


@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/UiFields")
@SwaggerDefinition(tags = {
		@Tag(name="UiFields",description = "UiFields Services")
})
@Api(tags = {"UiFields"})

public class UiFieldRESTService implements RestServicePlugin {

	@Inject
	@PluginInfo(version = 1)
	private UiFieldService service;


	@POST
	@Produces("application/json")
	@Read
	@ApiOperation(value = "listAllUiFields", notes = "List all Ui Fields")
	@Path("listAllUiFields")
	public List<UiField> listAllUiFields(
			@HeaderParam("authenticationKey") String authenticationKey,
			UiFieldFiltering uiFieldFiltering, @Context SecurityContext securityContext) {
		return service.listAllUiFields(uiFieldFiltering,securityContext);

	}


	@POST
	@Produces("application/json")
	@Write
	@ApiOperation(value = "linkUiFieldToClazz", notes = "Link Ui Field To Clazz")
	@Path("linkUiFieldToClazz")
	public UiFieldToClazz linkUiFieldToClazz(
			@HeaderParam("authenticationKey") String authenticationKey,
			LinkUiFieldRequest linkUiFieldRequest, @Context SecurityContext securityContext) {
		UiField uiField=linkUiFieldRequest.getUiFieldId()!=null?service.getByIdOrNull(linkUiFieldRequest.getUiFieldId(),UiField.class,null,securityContext):null;
		if(uiField==null){
			throw new BadRequestException("no ui field with id "+linkUiFieldRequest.getUiFieldId());
		}
		linkUiFieldRequest.setUiField(uiField);

		Clazz clazz=linkUiFieldRequest.getClazzId()!=null?service.getByIdOrNull(linkUiFieldRequest.getClazzId(),Clazz.class,null,securityContext):null;
		if(clazz==null){
			throw new BadRequestException("no clazz with id "+linkUiFieldRequest.getClazzId());
		}
		linkUiFieldRequest.setClazz(clazz);
		return service.linkUiFieldToClazz(linkUiFieldRequest,securityContext);

	}


	@POST
	@Produces("application/json")
	@Write
	@ApiOperation(value = "updateUiFieldLink", notes = "Updates Ui Field To Clazz Lunk")
	@Path("updateUiFieldLink")
	public UiFieldToClazz updateUiFieldLink(
			@HeaderParam("authenticationKey") String authenticationKey,
			UpdateUiFieldLink updateUiFieldLink, @Context SecurityContext securityContext) {
		UiFieldToClazz uiFieldToClazz=updateUiFieldLink.getId()!=null?service.getByIdOrNull(updateUiFieldLink.getId(),UiFieldToClazz.class,null,securityContext):null;
		if(uiFieldToClazz==null){
			throw new BadRequestException("no link with id "+updateUiFieldLink.getId());
		}
		updateUiFieldLink.setLink(uiFieldToClazz);

		return service.updateUiFieldToClazzLink(updateUiFieldLink,securityContext);

	}





}
