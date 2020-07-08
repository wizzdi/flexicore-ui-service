package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.GridPresetFiltering;

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
public class GridPresetRepository extends AbstractRepositoryPlugin {

	public List<GridPreset> listAllGridPresets(
			GridPresetFiltering gridPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GridPreset> q = cb.createQuery(GridPreset.class);
		Root<GridPreset> r = q.from(GridPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addGridPresetPredicates(preds, cb, r, gridPresetFiltering);
		QueryInformationHolder<GridPreset> queryInformationHolder = new QueryInformationHolder<>(
				gridPresetFiltering, GridPreset.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addGridPresetPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<GridPreset> r,
			GridPresetFiltering gridPresetFiltering) {

	}

	public long countAllGridPresets(GridPresetFiltering gridPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<GridPreset> r = q.from(GridPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addGridPresetPredicates(preds, cb, r, gridPresetFiltering);
		QueryInformationHolder<GridPreset> queryInformationHolder = new QueryInformationHolder<>(
				gridPresetFiltering, GridPreset.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
