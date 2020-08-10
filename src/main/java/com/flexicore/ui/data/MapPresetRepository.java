package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.MapPreset;
import com.flexicore.ui.request.MapPresetFiltering;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class MapPresetRepository extends AbstractRepositoryPlugin {

	public List<MapPreset> listAllMapPresets(
			MapPresetFiltering mapPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MapPreset> q = cb
				.createQuery(MapPreset.class);
		Root<MapPreset> r = q.from(MapPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addMapPresetPredicates(preds, cb, r,
				mapPresetFiltering);
		QueryInformationHolder<MapPreset> queryInformationHolder = new QueryInformationHolder<>(
				mapPresetFiltering, MapPreset.class,
				securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addMapPresetPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<MapPreset> r,
			MapPresetFiltering mapPresetFiltering) {
		PresetRepository.addPresetPredicates(preds,cb,r,mapPresetFiltering);


	}

	public long countAllMapPresets(
			MapPresetFiltering mapPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<MapPreset> r = q.from(MapPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addMapPresetPredicates(preds, cb, r,
				mapPresetFiltering);
		QueryInformationHolder<MapPreset> queryInformationHolder = new QueryInformationHolder<>(
				mapPresetFiltering, MapPreset.class,
				securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
