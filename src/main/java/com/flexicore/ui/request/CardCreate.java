package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.ui.model.CardGroup;

public class CardCreate extends BaseclassCreate {


	private String cardGroupId;
	@JsonIgnore
	private CardGroup cardGroup;


	public String getCardGroupId() {
		return cardGroupId;
	}

	public <T extends CardCreate> T setCardGroupId(
			String cardGroupId) {
		this.cardGroupId = cardGroupId;
		return (T) this;
	}
	@JsonIgnore
	public CardGroup getCardGroup() {
		return cardGroup;
	}

	public <T extends CardCreate> T setCardGroup(
			CardGroup cardGroup) {
		this.cardGroup = cardGroup;
		return (T) this;
	}

	@Override
	public boolean supportingDynamic() {
		return true;
	}
}
