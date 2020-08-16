package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.CardGroup;
import com.flexicore.ui.request.CardGroupCreate;
import com.flexicore.ui.request.CardGroupFiltering;
import com.flexicore.ui.request.CardGroupUpdate;
import com.flexicore.ui.service.CardGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Path("plugins/CardGroups")
@Tag(name = "CardGroups")
@Extension
@Component
public class CardGroupRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CardGroupService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllCardGroups", description = "returns all CardGroups")
	@Path("getAllCardGroups")
	public PaginationResponse<CardGroup> getAllCardGroups(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardGroupFiltering cardGroupFiltering,
			@Context SecurityContext securityContext) {
		service.validate(cardGroupFiltering,securityContext);
		return service.getAllCardGroups(cardGroupFiltering,
				securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateCardGroup", description = "Updates Dashbaord")
	@Path("updateCardGroup")
	public CardGroup updateCardGroup(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardGroupUpdate updateCardGroup,
			@Context SecurityContext securityContext) {
		CardGroup cardGroupToClazz = updateCardGroup
				.getId() != null ? service.getByIdOrNull(
				updateCardGroup.getId(), CardGroup.class, null,
				securityContext) : null;
		if (cardGroupToClazz == null) {
			throw new BadRequestException("no ui field with id  "
					+ updateCardGroup.getId());
		}
		updateCardGroup.setCardGroup(cardGroupToClazz);
		service.validate(updateCardGroup,securityContext);
		return service.updateCardGroup(updateCardGroup,
				securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createCardGroup", description = "Creates Ui Field ")
	@Path("createCardGroup")
	public CardGroup createCardGroup(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardGroupCreate createCardGroup,
			@Context SecurityContext securityContext) {
		service.validate(createCardGroup, securityContext);
		return service.createCardGroup(createCardGroup,
				securityContext);

	}

}
