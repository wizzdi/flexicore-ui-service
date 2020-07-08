package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.DashboardElement;
import com.flexicore.ui.request.DashboardElementFiltering;

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
public class DashboardElementRepository extends AbstractRepositoryPlugin {

	public List<DashboardElement> listAllDashboardElements(
			DashboardElementFiltering dashboardElementFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DashboardElement> q = cb
				.createQuery(DashboardElement.class);
		Root<DashboardElement> r = q.from(DashboardElement.class);
		List<Predicate> preds = new ArrayList<>();
		addDashboardElementPredicates(preds, cb, r, dashboardElementFiltering);
		QueryInformationHolder<DashboardElement> queryInformationHolder = new QueryInformationHolder<>(
				dashboardElementFiltering, DashboardElement.class,
				securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addDashboardElementPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<DashboardElement> r,
			DashboardElementFiltering dashboardElementFiltering) {

	}

	public long countAllDashboardElements(
			DashboardElementFiltering dashboardElementFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<DashboardElement> r = q.from(DashboardElement.class);
		List<Predicate> preds = new ArrayList<>();
		addDashboardElementPredicates(preds, cb, r, dashboardElementFiltering);
		QueryInformationHolder<DashboardElement> queryInformationHolder = new QueryInformationHolder<>(
				dashboardElementFiltering, DashboardElement.class,
				securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
