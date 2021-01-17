package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.PresetToPreset;
import com.flexicore.ui.model.PresetToPreset_;
import com.flexicore.ui.model.Preset_;
import com.flexicore.ui.request.PresetToPresetFiltering;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class PresetToPresetRepository extends AbstractRepositoryPlugin {

	public List<PresetToPreset> listAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PresetToPreset> q = cb.createQuery(PresetToPreset.class);
		Root<PresetToPreset> r = q.from(PresetToPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addPresetToPresetPredicates(preds, cb, r, presetToPresetFiltering);
		QueryInformationHolder<PresetToPreset> queryInpresetToPresetationHolder = new QueryInformationHolder<>(
				presetToPresetFiltering, PresetToPreset.class, securityContext);
		return getAllFiltered(queryInpresetToPresetationHolder, preds, cb, q, r);
	}

	private void addPresetToPresetPredicates(List<Predicate> preds, CriteriaBuilder cb,
			Root<PresetToPreset> r, PresetToPresetFiltering presetToPresetFiltering) {

		if(presetToPresetFiltering.getParentPresets()!=null&&!presetToPresetFiltering.getParentPresets().isEmpty()){
			Set<String> ids=presetToPresetFiltering.getParentPresets().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<PresetToPreset, Preset> join=r.join(PresetToPreset_.parentPreset);
			preds.add(join.get(Preset_.id).in(ids));
		}

		if(presetToPresetFiltering.getChildPresets()!=null&&!presetToPresetFiltering.getChildPresets().isEmpty()){
			Set<String> ids=presetToPresetFiltering.getChildPresets().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<PresetToPreset, Preset> join=r.join(PresetToPreset_.childPreset);
			preds.add(join.get(Preset_.id).in(ids));
		}


	}

	public long countAllPresetToPresets(PresetToPresetFiltering presetToPresetFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<PresetToPreset> r = q.from(PresetToPreset.class);
		List<Predicate> preds = new ArrayList<>();
		addPresetToPresetPredicates(preds, cb, r, presetToPresetFiltering);
		QueryInformationHolder<PresetToPreset> queryInpresetToPresetationHolder = new QueryInformationHolder<>(
				presetToPresetFiltering, PresetToPreset.class, securityContext);
		return countAllFiltered(queryInpresetToPresetationHolder, preds, cb, q, r);
	}
}
