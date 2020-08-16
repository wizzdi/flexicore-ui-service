package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Card;

public class CardUpdate extends CardCreate {
	private String id;
	@JsonIgnore
	private Card card;

	public String getId() {
		return id;
	}

	public <T extends CardUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public Card getCard() {
		return card;
	}

	public <T extends CardUpdate> T setCard(
			Card card) {
		this.card = card;
		return (T) this;
	}
}
