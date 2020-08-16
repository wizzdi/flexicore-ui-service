package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.CardGroup;

public class CardGroupUpdate extends CardGroupCreate {
	private String id;
	@JsonIgnore
	private CardGroup cardGroup;

	public String getId() {
		return id;
	}

	public <T extends CardGroupUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public CardGroup getCardGroup() {
		return cardGroup;
	}

	public <T extends CardGroupUpdate> T setCardGroup(
			CardGroup cardGroup) {
		this.cardGroup = cardGroup;
		return (T) this;
	}
}
