package com.flexicore.ui.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Card;
import com.flexicore.ui.request.CardCreate;
import com.flexicore.ui.request.CardFiltering;
import com.flexicore.ui.request.CardUpdate;
import com.flexicore.ui.service.CardService;
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
@Path("plugins/Cards")
@Tag(name = "Cards")
@Extension
@Component
public class CardRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private CardService service;

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllCards", description = "returns all Cards")
	@Path("getAllCards")
	public PaginationResponse<Card> getAllCards(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardFiltering cardFiltering,
			@Context SecurityContext securityContext) {
		service.validate(cardFiltering,securityContext);
		return service.getAllCards(cardFiltering, securityContext);

	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateCard", description = "Updates Dashbaord")
	@Path("updateCard")
	public Card updateCard(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardUpdate updateCard,
			@Context SecurityContext securityContext) {
		Card cardToClazz = updateCard.getId() != null ? service.getByIdOrNull(updateCard.getId(), Card.class, null, securityContext) : null;
		if (cardToClazz == null) {
			throw new BadRequestException("no ui field with id  " + updateCard.getId());
		}
		updateCard.setCard(cardToClazz);
		service.validate(updateCard,securityContext);
		return service.updateCard(updateCard, securityContext);

	}

	@POST
	@Produces("application/json")
	@Operation(summary = "createCard", description = "Creates Ui Field ")
	@Path("createCard")
	public Card createCard(
			@HeaderParam("authenticationKey") String authenticationKey,
			CardCreate createCard,
			@Context SecurityContext securityContext) {
		service.validate(createCard, securityContext);
		return service.createCard(createCard, securityContext);

	}

}
