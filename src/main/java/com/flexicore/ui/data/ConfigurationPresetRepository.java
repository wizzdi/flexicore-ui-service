package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.ConfigurationPreset;
import com.flexicore.ui.request.ConfigurationPresetFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class ConfigurationPresetRepository extends AbstractRepositoryPlugin {


    public List<ConfigurationPreset> listAllConfigurationPresets(ConfigurationPresetFiltering configurationPresetFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ConfigurationPreset> q=cb.createQuery(ConfigurationPreset.class);
        Root<ConfigurationPreset> r=q.from(ConfigurationPreset.class);
        List<Predicate> preds=new ArrayList<>();
        addConfigurationPresetPredicates(preds,cb,r,configurationPresetFiltering);
        QueryInformationHolder<ConfigurationPreset> queryInformationHolder=new QueryInformationHolder<>(configurationPresetFiltering,ConfigurationPreset.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addConfigurationPresetPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<ConfigurationPreset> r, ConfigurationPresetFiltering configurationPresetFiltering) {

    }

    public long countAllConfigurationPresets(ConfigurationPresetFiltering configurationPresetFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ConfigurationPreset> r=q.from(ConfigurationPreset.class);
        List<Predicate> preds=new ArrayList<>();
        addConfigurationPresetPredicates(preds,cb,r,configurationPresetFiltering);
        QueryInformationHolder<ConfigurationPreset> queryInformationHolder=new QueryInformationHolder<>(configurationPresetFiltering,ConfigurationPreset.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
