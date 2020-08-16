package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.CardGroup;
import com.flexicore.ui.request.CardGroupFiltering;

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
public class CardGroupRepository extends AbstractRepositoryPlugin {

	public List<CardGroup> listAllCardGroups(
			CardGroupFiltering cardGroupFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CardGroup> q = cb
				.createQuery(CardGroup.class);
		Root<CardGroup> r = q.from(CardGroup.class);
		List<Predicate> preds = new ArrayList<>();
		addCardGroupPredicates(preds, cb, r, cardGroupFiltering);
		QueryInformationHolder<CardGroup> queryInformationHolder = new QueryInformationHolder<>(
				cardGroupFiltering, CardGroup.class,
				securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addCardGroupPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<CardGroup> r,
			CardGroupFiltering cardGroupFiltering) {

	}

	public long countAllCardGroups(
			CardGroupFiltering cardGroupFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<CardGroup> r = q.from(CardGroup.class);
		List<Predicate> preds = new ArrayList<>();
		addCardGroupPredicates(preds, cb, r, cardGroupFiltering);
		QueryInformationHolder<CardGroup> queryInformationHolder = new QueryInformationHolder<>(
				cardGroupFiltering, CardGroup.class,
				securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
