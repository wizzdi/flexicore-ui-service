package com.flexicore.ui.service;


import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.FilterPropertiesRepository;
import com.flexicore.ui.model.FilterProperties;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.Preset;
import com.flexicore.ui.model.Preset_;
import com.flexicore.ui.request.FilterPropertiesCreate;
import com.flexicore.ui.request.FilterPropertiesFiltering;
import com.flexicore.ui.request.FilterPropertiesUpdate;
import com.flexicore.ui.request.GridPresetFiltering;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;


@Extension
@Component
public class FilterPropertiesService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(FilterPropertiesService.class);


    @Autowired
    private FilterPropertiesRepository filterPropertiesRepository;

    @Autowired
    private BasicService basicService;

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private GridPresetService gridPresetService;
    @Autowired
    private SecurityContextBase adminSecurityContextBase;
    @Autowired
    @Lazy
    @Qualifier("gridPresetClazz")
    private Clazz gridPresetClazz;



    public FilterProperties updateFilterProperties(FilterPropertiesUpdate filterPropertiesUpdate, SecurityContextBase securityContext) {
        if (FilterPropertiesUpdateNoMerge(filterPropertiesUpdate,
                filterPropertiesUpdate.getFilterProperties())) {
            filterPropertiesRepository.merge(filterPropertiesUpdate.getFilterProperties());
        }
        return filterPropertiesUpdate.getFilterProperties();
    }

    public boolean FilterPropertiesUpdateNoMerge(FilterPropertiesCreate filterPropertiesCreate, FilterProperties filterProperties) {
        boolean update = basicService.updateBasicNoMerge(filterPropertiesCreate, filterProperties);

        if (filterPropertiesCreate.getFilterPath() != null && !filterPropertiesCreate.getFilterPath().equals(filterProperties.getFilterPath())) {
            filterProperties.setFilterPath(filterPropertiesCreate.getFilterPath());
            update = true;
        }

        if (filterPropertiesCreate.getExternalize() != null && filterPropertiesCreate.getExternalize() != filterProperties.isExternalize()) {
            filterProperties.setExternalize(filterPropertiesCreate.getExternalize());
            update = true;
        }

        if (filterPropertiesCreate.getBaseclass() != null && (filterProperties.getRelatedBaseclass() == null || !filterPropertiesCreate.getBaseclass().getId().equals(filterProperties.getRelatedBaseclass().getId()))) {
            filterProperties.setRelatedBaseclass(filterPropertiesCreate.getBaseclass());
            update = true;
        }


        return update;
    }


    public List<FilterProperties> listAllFilterProperties(
            FilterPropertiesFiltering filterPropertiesFiltering,
            SecurityContextBase securityContext) {
        return filterPropertiesRepository.listAllFilterProperties(filterPropertiesFiltering,
                securityContext);
    }

    public FilterProperties createFilterProperties(FilterPropertiesCreate createFilterProperties,
                                                   SecurityContextBase securityContext) {
        FilterProperties filterProperties = createFilterPropertiesNoMerge(createFilterProperties,
                securityContext);
        filterPropertiesRepository.merge(filterProperties);
        return filterProperties;

    }

    public FilterProperties createFilterPropertiesNoMerge(
            FilterPropertiesCreate createFilterProperties, SecurityContextBase securityContext) {
        FilterProperties filterProperties = new FilterProperties();
        filterProperties.setId(UUID.randomUUID().toString());
        FilterPropertiesUpdateNoMerge(createFilterProperties, filterProperties);
        BaseclassService.createSecurityObjectNoMerge(filterProperties, securityContext);
        return filterProperties;
    }

    public PaginationResponse<FilterProperties> getAllFilterProperties(
            FilterPropertiesFiltering filterPropertiesFiltering,
            SecurityContextBase securityContext) {
        List<FilterProperties> list = listAllFilterProperties(filterPropertiesFiltering, securityContext);
        long count = filterPropertiesRepository.countAllFilterProperties(filterPropertiesFiltering, securityContext);
        return new PaginationResponse<>(list, filterPropertiesFiltering, count);
    }

    public void validate(FilterPropertiesCreate createFilterProperties,
                         SecurityContextBase securityContext) {
        basicService.validate(createFilterProperties, securityContext);
        String baseclassId = createFilterProperties.getBaseclassId();
        Preset baseclass = baseclassId != null ? getByIdOrNull(baseclassId, Preset.class, Preset_.security, securityContext) : null;
        if (baseclass == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Preset with id " + baseclassId);
        }
        createFilterProperties.setBaseclass(baseclass);
    }

    public void validate(FilterPropertiesFiltering filterPropertiesFiltering,
                         SecurityContextBase securityContext) {
        basicService.validate(filterPropertiesFiltering, securityContext);
        Set<String> baseclassIds = filterPropertiesFiltering.getBaseclassIds();
        Map<String, Preset> baseclassMap = baseclassIds.isEmpty() ? new HashMap<>() : filterPropertiesRepository.listByIds(Preset.class, baseclassIds,Preset_.security, securityContext).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
        baseclassIds.removeAll(baseclassMap.keySet());
        if (!baseclassIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Presets with ids " + baseclassIds);
        }
        filterPropertiesFiltering.setBaseclasses(new ArrayList<>(baseclassMap.values()));
    }


    @EventListener
    @Async
    public void handlePresetPermissionGroupCreated(BasicCreated<PermissionGroupToBaseclass> baseclassCreated) {
        PermissionGroupToBaseclass permissionGroupToBaseclass = baseclassCreated.getBaseclass();
        PermissionGroup permissionGroup = permissionGroupToBaseclass.getLeftside();
        if (permissionGroupToBaseclass.getRightside().getClazz().getId().equals(gridPresetClazz.getId()) ) {
            SecurityContextBase securityContext = adminSecurityContextBase;
            Baseclass baseclass = permissionGroupToBaseclass.getRightside();

            List<GridPreset> presets = gridPresetService.listAllGridPresets(new GridPresetFiltering().setRelatedBaseclass(Collections.singletonList(baseclass)), null);
            for (GridPreset preset : presets) {
                logger.info("grid preset " + preset.getName() + "(" + preset.getId() + ") was attached to permission group " + permissionGroup.getName() + "(" + permissionGroup.getId() + ") , will attach filter properties");
                List<FilterProperties> filterProperties = listAllFilterProperties(new FilterPropertiesFiltering().setBaseclasses(Collections.singletonList(preset)), securityContext);
                for (FilterProperties filterProperty : filterProperties) {
                    PermissionGroupToBaseclassCreate createPermissionGroupLinkRequest = new PermissionGroupToBaseclassCreate()
                            .setPermissionGroup(permissionGroup)
                            .setBaseclass(filterProperty.getSecurity());
                    permissionGroupToBaseclassService.createPermissionGroupToBaseclass(createPermissionGroupLinkRequest, securityContext);
                }


            }


        }

    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return filterPropertiesRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return filterPropertiesRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return filterPropertiesRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return filterPropertiesRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return filterPropertiesRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return filterPropertiesRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return filterPropertiesRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        filterPropertiesRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        filterPropertiesRepository.massMerge(toMerge);
    }
}
