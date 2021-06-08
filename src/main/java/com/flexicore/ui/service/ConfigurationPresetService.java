package com.flexicore.ui.service;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.ConfigurationPresetRepository;
import com.flexicore.ui.model.ConfigurationPreset;
import com.flexicore.ui.request.ConfigurationPresetCreate;
import com.flexicore.ui.request.ConfigurationPresetFiltering;
import com.flexicore.ui.request.ConfigurationPresetUpdate;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Extension
@Component
public class ConfigurationPresetService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationPresetService.class);


    @Autowired
    private ConfigurationPresetRepository configurationPresetRepository;


    @Autowired
    private PresetService presetService;

    public ConfigurationPreset updateConfigurationPreset(ConfigurationPresetUpdate updateConfigurationPreset, SecurityContextBase securityContext) {
        if (updateConfigurationPresetNoMerge(updateConfigurationPreset, updateConfigurationPreset.getConfigurationPreset())) {
            configurationPresetRepository.merge(updateConfigurationPreset.getConfigurationPreset());
        }
        return updateConfigurationPreset.getConfigurationPreset();
    }

    public boolean updateConfigurationPresetNoMerge(
            ConfigurationPresetCreate createConfigurationPreset,
            ConfigurationPreset configurationPreset) {
        boolean update = presetService.updatePresetNoMerge(createConfigurationPreset, configurationPreset);
        if (createConfigurationPreset.getConfigurationUI() != null && !createConfigurationPreset.getConfigurationUI().equals(configurationPreset.getConfigurationUI())) {
            configurationPreset.setConfigurationUI(createConfigurationPreset.getConfigurationUI());
            update = true;
        }
        return update;
    }


    public PaginationResponse<ConfigurationPreset> getAllConfigurationPresets(ConfigurationPresetFiltering configurationPresetFiltering, SecurityContextBase securityContext) {
        List<ConfigurationPreset> list = listAllConfigurationPresets(configurationPresetFiltering, securityContext);
        long count = configurationPresetRepository.countAllConfigurationPresets(configurationPresetFiltering, securityContext);
        return new PaginationResponse<>(list, configurationPresetFiltering,
                count);
    }

    public List<ConfigurationPreset> listAllConfigurationPresets(ConfigurationPresetFiltering configurationPresetFiltering, SecurityContextBase securityContext) {
        return configurationPresetRepository.listAllConfigurationPresets(configurationPresetFiltering, securityContext);
    }

    public ConfigurationPreset createConfigurationPreset(ConfigurationPresetCreate createConfigurationPreset, SecurityContextBase securityContext) {
        ConfigurationPreset configurationPreset = createConfigurationPresetNoMerge(createConfigurationPreset, securityContext);
        configurationPresetRepository.merge(configurationPreset);
        return configurationPreset;

    }

    private ConfigurationPreset createConfigurationPresetNoMerge(ConfigurationPresetCreate createConfigurationPreset, SecurityContextBase securityContext) {
        ConfigurationPreset configurationPreset = new ConfigurationPreset();
        configurationPreset.setId(UUID.randomUUID().toString());
        updateConfigurationPresetNoMerge(createConfigurationPreset, configurationPreset);
        BaseclassService.createSecurityObjectNoMerge(configurationPreset, securityContext);
        return configurationPreset;
    }

    public void validate(ConfigurationPresetCreate createConfigurationPreset, SecurityContextBase securityContext) {
        presetService.validate(createConfigurationPreset, securityContext);

    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return configurationPresetRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return configurationPresetRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return configurationPresetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return configurationPresetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return configurationPresetRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return configurationPresetRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return configurationPresetRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        configurationPresetRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        configurationPresetRepository.massMerge(toMerge);
    }
}
