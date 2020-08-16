package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Card;
import com.flexicore.ui.request.CardFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class CardRepository extends AbstractRepositoryPlugin {

	public List<Card> listAllCards(CardFiltering cardFiltering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Card> q = cb.createQuery(Card.class);
		Root<Card> r = q.from(Card.class);
		List<Predicate> preds = new ArrayList<>();
		addCardPredicates(preds, cb, r, cardFiltering);
		QueryInformationHolder<Card> queryInformationHolder = new QueryInformationHolder<>(cardFiltering, Card.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addCardPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<Card> r, CardFiltering cardFiltering) {

	}

	public long countAllCards(CardFiltering cardFiltering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Card> r = q.from(Card.class);
		List<Predicate> preds = new ArrayList<>();
		addCardPredicates(preds, cb, r, cardFiltering);
		QueryInformationHolder<Card> queryInformationHolder = new QueryInformationHolder<>(cardFiltering, Card.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
