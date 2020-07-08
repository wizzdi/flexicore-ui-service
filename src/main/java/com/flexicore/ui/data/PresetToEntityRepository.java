package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.flexicore.model.Baselink_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.PresetToEntity;
import com.flexicore.ui.model.PresetToEntity_;
import com.flexicore.ui.request.PresetToEntityFiltering;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class PresetToEntityRepository extends AbstractRepositoryPlugin {

	public List<PresetToEntity> listAllPresetToEntities(
			PresetToEntityFiltering presetToEntityFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PresetToEntity> q = cb.createQuery(PresetToEntity.class);
		Root<PresetToEntity> r = q.from(PresetToEntity.class);
		List<Predicate> preds = new ArrayList<>();
		addPresetToEntityPredicates(preds, cb, r, presetToEntityFiltering);
		QueryInformationHolder<PresetToEntity> queryInformationHolder = new QueryInformationHolder<>(
				presetToEntityFiltering, PresetToEntity.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addPresetToEntityPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<PresetToEntity> r,
			PresetToEntityFiltering presetToEntityFiltering) {
		if (presetToEntityFiltering.getEnabled() != null) {
			preds.add(cb.equal(r.get(PresetToEntity_.enabled),
					presetToEntityFiltering.getEnabled()));
		}
		if (presetToEntityFiltering.getRightside() != null
				&& !presetToEntityFiltering.getRightside().isEmpty()) {
			Set<String> ids = presetToEntityFiltering.getRightside()
					.parallelStream().map(f -> f.getId())
					.collect(Collectors.toSet());
			Join<PresetToEntity, Baseclass> join = r.join(Baselink_.rightside);
			preds.add(join.get(Baseclass_.id).in(ids));
		}
	}

	public long countAllPresetToEntities(
			PresetToEntityFiltering presetToEntityFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<PresetToEntity> r = q.from(PresetToEntity.class);
		List<Predicate> preds = new ArrayList<>();
		addPresetToEntityPredicates(preds, cb, r, presetToEntityFiltering);
		QueryInformationHolder<PresetToEntity> queryInformationHolder = new QueryInformationHolder<>(
				presetToEntityFiltering, PresetToEntity.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
