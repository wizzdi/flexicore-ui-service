package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.Preset_;
import com.flexicore.ui.request.PresetFiltering;
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
public class PresetRepository extends AbstractRepositoryPlugin {

    public List<Preset> listAllPresets(PresetFiltering presetFiltering,
                                       SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Preset> q = cb.createQuery(Preset.class);
        Root<Preset> r = q.from(Preset.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetPredicates(preds, cb, r, presetFiltering);
        QueryInformationHolder<Preset> queryInformationHolder = new QueryInformationHolder<>(
                presetFiltering, Preset.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    public static <T extends Preset> void addPresetPredicates(List<Predicate> preds, CriteriaBuilder cb,
                                     Root<T> r, PresetFiltering presetFiltering) {
        if (presetFiltering.getExternalIds() != null && !presetFiltering.getExternalIds().isEmpty()) {
            preds.add(r.get(Preset_.externalId).in(presetFiltering.getExternalIds()));
        }

    }

    public long countAllPresets(PresetFiltering presetFiltering,
                                SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Preset> r = q.from(Preset.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetPredicates(preds, cb, r, presetFiltering);
        QueryInformationHolder<Preset> queryInformationHolder = new QueryInformationHolder<>(
                presetFiltering, Preset.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }
}
