package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.ui.data.CardRepository;
import com.flexicore.ui.model.Card;
import com.flexicore.ui.model.CardGroup;
import com.flexicore.ui.request.CardCreate;
import com.flexicore.ui.request.CardFiltering;
import com.flexicore.ui.request.CardUpdate;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class CardService implements ServicePlugin {

    @Autowired
    private Logger logger;

    @PluginInfo(version = 1)
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BaseclassNewService baseclassNewService;

    public Card updateCard(CardUpdate updateCard, SecurityContext securityContext) {
        if (updateCardNoMerge(updateCard, updateCard.getCard())) {
            cardRepository.merge(updateCard.getCard());
        }
        return updateCard.getCard();
    }

    public boolean updateCardNoMerge(CardCreate updateCardGroup, Card cardGroup) {
        boolean update = baseclassNewService.updateBaseclassNoMerge(updateCardGroup, cardGroup);
        if (updateCardGroup.getCardGroup() != null
                && (cardGroup.getCardGroup() == null || !updateCardGroup
                .getCardGroup().getId()
                .equals(cardGroup.getCardGroup().getId()))) {
            update = true;
            cardGroup.setCardGroup(updateCardGroup
                    .getCardGroup());
        }
        return update;
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
                                                 List<String> batchString, SecurityContext securityContext) {
        return cardRepository.getByIdOrNull(id, c, batchString,
                securityContext);
    }

    public PaginationResponse<Card> getAllCards(
            CardFiltering cardFiltering,
            SecurityContext securityContext) {
        List<Card> list = listAllCards(cardFiltering, securityContext);
        long count = cardRepository.countAllCards(cardFiltering, securityContext);
        return new PaginationResponse<>(list, cardFiltering, count);
    }

    public List<Card> listAllCards(
            CardFiltering cardFiltering,
            SecurityContext securityContext) {
        return cardRepository.listAllCards(cardFiltering, securityContext);
    }

    public Card createCard(CardCreate createCard, SecurityContext securityContext) {
        Card card = createCardNoMerge(createCard, securityContext);
        cardRepository.merge(card);
        return card;

    }

    private Card createCardNoMerge(CardCreate createCard, SecurityContext securityContext) {
        Card card = new Card(createCard.getName(), securityContext);
        updateCardNoMerge(createCard, card);
        return card;
    }

    public void validate(CardFiltering cardFiltering, SecurityContext securityContext) {
		Set<String> cardGroupIds=cardFiltering.getCardGroupIds();
		Map<String,CardGroup> cardMap=cardGroupIds.isEmpty()?new HashMap<>():cardRepository.listByIds(CardGroup.class,cardGroupIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		cardGroupIds.removeAll(cardMap.keySet());
		if(!cardGroupIds.isEmpty()){
			throw new BadRequestException("No CardGroup with ids "+cardGroupIds);
		}
		cardFiltering.setCardGroups(new ArrayList<>(cardMap.values()));
    }


    public void validate(CardCreate createCard, SecurityContext securityContext) {

        String cardGroupId = createCard.getCardGroupId();
        CardGroup cardGroup = cardGroupId != null ? getByIdOrNull(cardGroupId, CardGroup.class,
                null, securityContext) : null;
        if (cardGroup == null) {
            throw new BadRequestException("No dashboard element with id " + cardGroupId);
        }
        createCard.setCardGroup(cardGroup);


    }
}
