package com.flexicore.ui.service;


import com.flexicore.model.Basic;
import com.wizzdi.dynamic.properties.converter.DynamicPropertiesUtils;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.PresetRepository;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.request.PresetCreate;
import com.flexicore.ui.request.PresetFiltering;
import com.flexicore.ui.request.PresetUpdate;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;


@Extension
@Component
public class PresetService implements Plugin {

    private static final Logger logger=LoggerFactory.getLogger(PresetService.class);

    
    @Autowired
    private PresetRepository presetRepository;

    @Autowired
    private BasicService basicService;


    public Preset updatePreset(PresetUpdate updatePreset,
                               SecurityContextBase securityContext) {
        if (updatePresetNoMerge(updatePreset, updatePreset.getPreset())) {
            presetRepository.merge(updatePreset.getPreset());
        }
        return updatePreset.getPreset();
    }

    public boolean updatePresetNoMerge(PresetCreate createPreset, Preset preset) {
        boolean update = basicService.updateBasicNoMerge(createPreset, preset);

        if (createPreset.getExternalId() != null && (preset.getExternalId() == null || !createPreset.getExternalId().equals(preset.getExternalId()))) {
            preset.setExternalId(createPreset.getExternalId());

            update = true;
        }
        Map<String, Object> map = DynamicPropertiesUtils.updateDynamic(createPreset.any(), preset.any());
        if (map != null) {
            preset.setJsonNode(map);
            update = true;
        }

        return update;
    }


    public PaginationResponse<Preset> getAllPresets(
            PresetFiltering presetFiltering, SecurityContextBase securityContext) {
        List<Preset> list = listAllPresets(presetFiltering, securityContext);
        long count = presetRepository.countAllPresets(presetFiltering, securityContext);
        return new PaginationResponse<>(list, presetFiltering, count);
    }

    public List<Preset> listAllPresets(PresetFiltering presetFiltering,
                                       SecurityContextBase securityContext) {
        return presetRepository.listAllPresets(presetFiltering, securityContext);
    }

    public Preset createPreset(PresetCreate createPreset,
                               SecurityContextBase securityContext) {
        Preset preset = createPresetNoMerge(createPreset, securityContext);
        presetRepository.merge(preset);
        return preset;

    }


    private Preset createPresetNoMerge(PresetCreate createPreset,
                                       SecurityContextBase securityContext) {
        Preset preset = new Preset();
        preset.setId(UUID.randomUUID().toString());
        updatePresetNoMerge(createPreset, preset);
        BaseclassService.createSecurityObjectNoMerge(preset,securityContext);
        return preset;
    }

    public void validate(PresetCreate presetCreate, SecurityContextBase securityContext) {
        basicService.validate(presetCreate, securityContext);
    }

    public void validate(PresetFiltering presetFiltering, SecurityContextBase securityContext) {
        basicService.validate(presetFiltering, securityContext);
    }


    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return presetRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return presetRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return presetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return presetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return presetRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return presetRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return presetRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        presetRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        presetRepository.massMerge(toMerge);
    }
}
