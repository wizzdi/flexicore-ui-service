package com.flexicore.ui.service;


import com.flexicore.category.model.Category;
import com.flexicore.category.request.CategoryCreate;
import com.flexicore.category.request.CategoryFilter;
import com.flexicore.category.service.CategoryService;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.UiFieldRepository;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;
import com.flexicore.ui.response.PresetToRoleContainer;
import com.flexicore.ui.response.PresetToTenantContainer;
import com.flexicore.ui.response.PresetToUserContainer;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
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
public class UiFieldService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(UiFieldService.class);


    @Autowired
    private UiFieldRepository uiFieldRepository;


    @Autowired
    private PresetToEntityService presetToEntityService;

    @Autowired
    private BasicService basicService;
    @Autowired

    private CategoryService categoryService;

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private PresetService presetService;
    @Autowired
    @Qualifier("presetClazz")
    @Lazy
    private Clazz presetClazz;
    @Autowired
    private SecurityContextBase adminSecurityContextBase;

    public UiField updateUiField(UiFieldUpdate updateUiField, SecurityContextBase securityContext) {
        if (updateUiFieldNoMerge(updateUiField, updateUiField.getUiField())) {
            uiFieldRepository.merge(updateUiField.getUiField());
        }
        return updateUiField.getUiField();
    }

    public boolean updateUiFieldNoMerge(UiFieldCreate uiFieldCreate,
                                        UiField uiField) {
        boolean update = basicService.updateBasicNoMerge(uiFieldCreate, uiField);
        if (uiFieldCreate.getVisible() != null && uiFieldCreate.getVisible() != uiField.isVisible()) {
            update = true;
            uiField.setVisible(uiFieldCreate.getVisible());
        }
        if (uiFieldCreate.getDynamicField() != null && uiFieldCreate.getDynamicField() != uiField.isDynamicField()) {
            update = true;
            uiField.setDynamicField(uiFieldCreate.getDynamicField());
        }

        if (uiFieldCreate.getPriority() != null && uiFieldCreate.getPriority() != uiField.getPriority()) {
            update = true;
            uiField.setPriority(uiFieldCreate.getPriority());
        }


        if (uiFieldCreate.getCategory() != null && (uiField.getCategory() == null || !uiFieldCreate.getCategory().getId().equals(uiField.getCategory().getId()))) {
            update = true;
            uiField.setCategory(uiFieldCreate.getCategory());
        }

        if (uiFieldCreate.getPreset() != null && (uiField.getPreset() == null || !uiFieldCreate.getPreset().getId().equals(uiField.getPreset().getId()))) {
            update = true;
            uiField.setPreset(uiFieldCreate.getPreset());
        }

        if (uiFieldCreate.getDisplayName() != null && !uiFieldCreate.getDisplayName().equals(uiField.getDisplayName())) {
            update = true;
            uiField.setDisplayName(uiFieldCreate.getDisplayName());
        }

        return update;
    }

    public List<UiField> listAllUiFields(UiFieldFiltering uiFieldFiltering,
                                         SecurityContextBase securityContext) {
        return uiFieldRepository.listAllUiFields(uiFieldFiltering,
                securityContext);
    }

    public UiField createUiField(UiFieldCreate createUiField,
                                 SecurityContextBase securityContext) {
        UiField uiField = createUiFieldNoMerge(createUiField, securityContext);
        uiFieldRepository.merge(uiField);
        return uiField;

    }

    public UiField createUiFieldNoMerge(UiFieldCreate createUiField,
                                        SecurityContextBase securityContext) {
        UiField uiField = new UiField();
        uiField.setId(UUID.randomUUID().toString());
        updateUiFieldNoMerge(createUiField, uiField);
        BaseclassService.createSecurityObjectNoMerge(uiField, securityContext);
        return uiField;
    }

    public void validate(PresetToUserCreate linkPresetToUser,
                         SecurityContextBase securityContext) {
        presetToEntityService.validate(linkPresetToUser, securityContext);
        String userId = linkPresetToUser.getUserId();
        SecurityUser securityUser = userId != null ? getByIdOrNull(userId, SecurityUser.class, null, securityContext) : null;
        if (securityUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no securityUser with id " + userId);
        }
        linkPresetToUser.setUser(securityUser);
    }

    public void validate(PresetToRoleCreate presetToRoleCreate,
                         SecurityContextBase securityContext) {
        presetToEntityService.validate(presetToRoleCreate, securityContext);

        String roleId = presetToRoleCreate.getRoleId();
        Role role = roleId != null ? getByIdOrNull(roleId, Role.class, null, securityContext) : null;
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no role with id " + roleId);
        }
        presetToRoleCreate.setRole(role);
    }

    public void validate(PresetToTenantCreate presetToTenantCreate,
                         SecurityContextBase securityContext) {
        presetToEntityService.validate(presetToTenantCreate, securityContext);

        String tenantId = presetToTenantCreate.getPreferredTenantId();
        SecurityTenant securityTenant = tenantId != null ? getByIdOrNull(tenantId, SecurityTenant.class, null, securityContext) : null;
        if (securityTenant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no securityTenant with id " + tenantId);
        }
        presetToTenantCreate.setPreferredTenant(securityTenant);
    }

    public PresetToRole createPresetToRole(
            PresetToRoleCreate presetToRoleCreate,
            SecurityContextBase securityContext) {
        PresetToRole toRet;
        PresetToRoleFilter presetToRoleFilter = new PresetToRoleFilter()
                .setRoles(Collections.singletonList(presetToRoleCreate.getRole()))
                .setPresets(Collections.singletonList(presetToRoleCreate.getPreset()));
        List<PresetToRole> existing = listAllPresetToRole(presetToRoleFilter, securityContext);
        if (existing.isEmpty()) {
            toRet = createPresetToRoleNoMerge(presetToRoleCreate, securityContext);
            uiFieldRepository.merge(toRet);
        } else {
            toRet = existing.get(0);
        }

        return toRet;
    }

    private PresetToRole createPresetToRoleNoMerge(
            PresetToRoleCreate presetToRoleCreate,
            SecurityContextBase securityContext) {
        PresetToRole presetToRole = new PresetToRole();
        presetToRole.setId(UUID.randomUUID().toString());
        updatePresetToRoleNoMerge(presetToRoleCreate, presetToRole);
        BaseclassService.createSecurityObjectNoMerge(presetToRole, securityContext);
        return presetToRole;
    }

    public PresetToTenant createPresetToTenant(
            PresetToTenantCreate presetToTenantCreate,
            SecurityContextBase securityContext) {
        PresetToTenant toRet;
        PresetToTenantFilter presetToTenantFilter = new PresetToTenantFilter()
                .setTenants(Collections.singletonList(presetToTenantCreate.getPreferredTenant()))
                .setPresets(Collections.singletonList(presetToTenantCreate.getPreset()));
        List<PresetToTenant> existing = listAllPresetToTenant(presetToTenantFilter, securityContext);
        if (existing.isEmpty()) {
            toRet = createPresetToTenantNoMerge(presetToTenantCreate, securityContext);
            uiFieldRepository.merge(toRet);
        } else {
            toRet = existing.get(0);
        }

        return toRet;
    }

    public PresetToTenant createPresetToTenantNoMerge(
            PresetToTenantCreate presetToTenantCreate,
            SecurityContextBase securityContext) {
        PresetToTenant presetToTenant = new PresetToTenant();
        presetToTenant.setId(UUID.randomUUID().toString());
        updatePresetToTenantNoMerge(presetToTenantCreate, presetToTenant);
        BaseclassService.createSecurityObjectNoMerge(presetToTenant, securityContext);
        return presetToTenant;
    }

    public PresetToUser createPresetToUser(
            PresetToUserCreate presetToUserCreate,
            SecurityContextBase securityContext) {
        PresetToUser toRet;
        PresetToUserFilter presetToUserFilter = new PresetToUserFilter()
                .setUsers(Collections.singletonList(presetToUserCreate.getUser()))
                .setPresets(Collections.singletonList(presetToUserCreate.getPreset()));
        List<PresetToUser> existing = listAllPresetToUser(presetToUserFilter, securityContext);
        if (existing.isEmpty()) {
            toRet = createPresetToUserNoMerge(presetToUserCreate, securityContext);
            uiFieldRepository.merge(toRet);
        } else {
            toRet = existing.get(0);
        }

        return toRet;
    }

    private PresetToUser createPresetToUserNoMerge(
            PresetToUserCreate presetToUserCreate,
            SecurityContextBase securityContext) {
        PresetToUser presetToUser = new PresetToUser();
        presetToUser.setId(UUID.randomUUID().toString());
        updatePresetToUserNoMerge(presetToUserCreate, presetToUser);
        BaseclassService.createSecurityObjectNoMerge(presetToUser, securityContext);
        return presetToUser;
    }

    public boolean updatePresetToTenantNoMerge(
            PresetToTenantCreate presetToTenantCreate,
            PresetToTenant presetToTenant) {
        boolean update = presetToEntityService.presetToEntityUpdateNoMerge(presetToTenantCreate, presetToTenant);

        if (presetToTenantCreate.getPreferredTenant() != null && (presetToTenant.getEntity() == null || !presetToTenantCreate.getPreferredTenant().getId().equals(presetToTenant.getEntity().getId()))) {
            presetToTenant.setEntity(presetToTenantCreate.getPreferredTenant());
            update = true;
        }
        return update;
    }

    public PresetToTenant updatePresetToTenant(
            PresetToTenantUpdate updateLinkPresetToTenant,
            SecurityContextBase securityContext) {
        if (updatePresetToTenantNoMerge(updateLinkPresetToTenant, updateLinkPresetToTenant.getPresetToTenant())) {
            uiFieldRepository.merge(updateLinkPresetToTenant.getPresetToTenant());
        }
        return updateLinkPresetToTenant.getPresetToTenant();
    }

    public PresetToUser updatePresetToUser(
            PresetToUserUpdate updateLinkPresetToUser,
            SecurityContextBase securityContext) {
        if (updatePresetToUserNoMerge(updateLinkPresetToUser, updateLinkPresetToUser.getPresetToUser())) {
            uiFieldRepository.merge(updateLinkPresetToUser.getPresetToUser());
        }
        return updateLinkPresetToUser.getPresetToUser();
    }

    public boolean updatePresetToUserNoMerge(PresetToUserCreate presetToUserCreate, PresetToUser presetToUser) {
        boolean update = presetToEntityService.presetToEntityUpdateNoMerge(presetToUserCreate, presetToUser);
        if (presetToUserCreate.getUser() != null && (presetToUser.getEntity() == null || !presetToUserCreate.getUser().getId().equals(presetToUser.getEntity().getId()))) {
            presetToUser.setEntity(presetToUserCreate.getUser());
            update = true;
        }
        return update;
    }

    public PresetToRole updatePresetToRole(PresetToRoleUpdate updateLinkPresetToRole, SecurityContextBase securityContext) {
        if (updatePresetToRoleNoMerge(updateLinkPresetToRole, updateLinkPresetToRole.getPresetToRole())) {
            uiFieldRepository.merge(updateLinkPresetToRole.getPresetToRole());
        }
        return updateLinkPresetToRole.getPresetToRole();
    }

    public boolean updatePresetToRoleNoMerge(
            PresetToRoleCreate presetToRoleCreate, PresetToRole presetToRole) {
        boolean update = presetToEntityService.presetToEntityUpdateNoMerge(presetToRoleCreate, presetToRole);
        if (presetToRoleCreate.getRole() != null && (presetToRole.getEntity() == null || !presetToRoleCreate.getRole().getId().equals(presetToRole.getEntity().getId()))) {
            presetToRole.setEntity(presetToRoleCreate.getRole());
            update = true;
        }
        return update;
    }

    public void validate(PresetToRoleFilter presetToRoleFilter,
                         SecurityContextBase securityContext) {
        Set<String> roleIds = presetToRoleFilter.getRoleIds();
        Map<String, Role> map = roleIds.isEmpty() ? new HashMap<>() : uiFieldRepository.listByIds(Role.class, roleIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        roleIds.removeAll(map.keySet());
        if (!roleIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Roles with ids " + roleIds);
        }
        presetToRoleFilter.setRoles(new ArrayList<>(map.values()));

    }

    public PaginationResponse<PresetToRoleContainer> getAllPresetToRole(
            PresetToRoleFilter presetToRoleFilter,
            SecurityContextBase securityContext) {
        List<PresetToRoleContainer> list = listAllPresetToRole(presetToRoleFilter, securityContext).parallelStream().map(f -> new PresetToRoleContainer(f)).collect(Collectors.toList());
        long count = uiFieldRepository.countAllPresetToRoles(presetToRoleFilter, securityContext);
        return new PaginationResponse<>(list, presetToRoleFilter, count);
    }

    private List<PresetToRole> listAllPresetToRole(PresetToRoleFilter presetToRoleFilter, SecurityContextBase securityContext) {
        return uiFieldRepository.listAllPresetToRoles(presetToRoleFilter, securityContext);
    }

    public void validate(PresetToTenantFilter presetToTenantFilter, SecurityContextBase securityContext) {
        Set<String> tenantIds = presetToTenantFilter.getTenantIdsForPreset();
        Map<String, SecurityTenant> map = tenantIds.isEmpty() ? new HashMap<>() : uiFieldRepository.listByIds(SecurityTenant.class, tenantIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        tenantIds.removeAll(map.keySet());
        if (!tenantIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Tenants with ids " + tenantIds);
        }
        presetToTenantFilter.setTenants(new ArrayList<>(map.values()));

    }

    public PaginationResponse<PresetToTenantContainer> getAllPresetToTenant(PresetToTenantFilter presetToTenantFilter, SecurityContextBase securityContext) {
        List<PresetToTenantContainer> list = listAllPresetToTenant(presetToTenantFilter, securityContext).parallelStream().map(f -> new PresetToTenantContainer(f)).collect(Collectors.toList());
        long count = uiFieldRepository.countAllPresetToTenants(presetToTenantFilter, securityContext);
        return new PaginationResponse<>(list, presetToTenantFilter, count);
    }

    private List<PresetToTenant> listAllPresetToTenant(PresetToTenantFilter presetToTenantFilter, SecurityContextBase securityContext) {
        return uiFieldRepository.listAllPresetToTenants(presetToTenantFilter,
                securityContext);
    }

    public void validate(PresetToUserFilter presetToUserFilter, SecurityContextBase securityContext) {
        Set<String> userIds = presetToUserFilter.getUserIds();
        Map<String, SecurityUser> map = userIds.isEmpty() ? new HashMap<>() : uiFieldRepository.listByIds(SecurityUser.class, userIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        userIds.removeAll(map.keySet());
        if (!userIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Users with ids " + userIds);
        }
        presetToUserFilter.setUsers(new ArrayList<>(map.values()));

    }

    public PaginationResponse<PresetToUserContainer> getAllPresetToUser(PresetToUserFilter presetToUserFilter, SecurityContextBase securityContext) {
        List<PresetToUserContainer> list = listAllPresetToUser(presetToUserFilter, securityContext).parallelStream().map(f -> new PresetToUserContainer(f)).collect(Collectors.toList());
        long count = uiFieldRepository.countAllPresetToUsers(
                presetToUserFilter, securityContext);
        return new PaginationResponse<>(list, presetToUserFilter, count);
    }

    private List<PresetToUser> listAllPresetToUser(PresetToUserFilter presetToUserFilter, SecurityContextBase securityContext) {
        return uiFieldRepository.listAllPresetToUsers(presetToUserFilter,
                securityContext);
    }

    public PaginationResponse<UiField> getAllUiFields(
            UiFieldFiltering uiFieldFiltering, SecurityContextBase securityContext) {
        List<UiField> list = listAllUiFields(uiFieldFiltering, securityContext);
        long count = uiFieldRepository.countAllUiFields(uiFieldFiltering,
                securityContext);
        return new PaginationResponse<>(list, uiFieldFiltering, count);
    }

    public void validate(MassCreateUiFields massCreateUiFields,
                         SecurityContextBase securityContext) {
        GridPreset preset = massCreateUiFields.getGridPresetId() != null
                ? getByIdOrNull(massCreateUiFields.getGridPresetId(),
                GridPreset.class, null, securityContext) : null;
        if (preset == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no GridPreset with id "
                    + massCreateUiFields.getGridPresetId());
        }
        massCreateUiFields.setGridPreset(preset);
        Map<String, List<UiFieldCreate>> map = massCreateUiFields
                .getUiFields()
                .parallelStream()
                .filter(f -> f.getCategoryName() != null)
                .collect(
                        Collectors.groupingBy(f -> f.getCategoryName(),
                                Collectors.toList()));

        CategoryFilter categoryFilter = new CategoryFilter()
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(map.keySet()));
        Map<String, Category> categoryMap = categoryService
                .listAllCategories(categoryFilter, securityContext)
                .parallelStream()
                .collect(Collectors.toMap(f -> f.getName(), f -> f, (a, b) -> a));
        for (Map.Entry<String, List<UiFieldCreate>> entry : map.entrySet()) {
            Category category = categoryMap.computeIfAbsent(entry.getKey(), f -> categoryService.createCategory(new CategoryCreate().setName(f), securityContext));
            for (UiFieldCreate createUiField : entry.getValue()) {
                createUiField.setCategory(category);
            }
        }

    }

    public void validate(UiFieldCreate createUiField, SecurityContextBase securityContext) {
        basicService.validate(createUiField, securityContext);
        Category category = categoryService.createCategory(
                new CategoryCreate().setName(createUiField.getCategoryName()),
                securityContext);
        createUiField.setCategory(category);
        String presetId = createUiField.getPresetId();
        Preset preset = presetId != null ? getByIdOrNull(presetId, Preset.class, Preset_.security, securityContext) : null;
        if (presetId!=null&&preset == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Preset with id " + presetId);
        }
        createUiField.setPreset(preset);
    }

    public List<UiField> massCreateUiFields(
            MassCreateUiFields massCreateUiFields,
            SecurityContextBase securityContext) {
        List<UiField> toMerge = new ArrayList<>();
        for (UiFieldCreate uiField : massCreateUiFields.getUiFields()) {
            toMerge.add(createUiFieldNoMerge(
                    uiField.setPreset(massCreateUiFields.getGridPreset()),
                    securityContext));
        }
        uiFieldRepository.massMerge(toMerge);
        return toMerge;
    }

    public void validate(UiFieldFiltering uiFieldFiltering,
                         SecurityContextBase securityContext) {
        basicService.validate(uiFieldFiltering, securityContext);
        Set<String> gridPresetIds = uiFieldFiltering.getPresetIds();
        Map<String, GridPreset> gridPresetMap = gridPresetIds.isEmpty() ? new HashMap<>() : uiFieldRepository.listByIds(GridPreset.class, gridPresetIds, GridPreset_.security, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        gridPresetIds.removeAll(gridPresetMap.keySet());
        if (!gridPresetIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Grid Presets with ids " + gridPresetIds);
        }
        uiFieldFiltering.setPresets(new ArrayList<>(gridPresetMap.values()));
    }

    public UiFieldCreate getUIFieldCreate(UiField uiField) {
        return new UiFieldCreate().setPreset(uiField.getPreset())
                .setCategory(uiField.getCategory())
                .setDisplayName(uiField.getDisplayName())
                .setVisible(uiField.isVisible())
                .setPriority(uiField.getPriority())
                .setDescription(uiField.getDescription())
                .setName(uiField.getName());
    }

    public void validate(PreferedPresetRequest preferedPresetRequest,
                         SecurityContextBase securityContext) {

    }

    @EventListener
    @Async
    public void handlePresetPermissionGroupCreated(BasicCreated<PermissionGroupToBaseclass> baseclassCreated) {
        PermissionGroupToBaseclass permissionGroupToBaseclass = baseclassCreated.getBaseclass();
        PermissionGroup permissionGroup = permissionGroupToBaseclass.getLeftside();
        if (permissionGroupToBaseclass.getRightside().getClazz().getId().equals(presetClazz.getId())) {
            Baseclass baseclass = permissionGroupToBaseclass.getRightside();
            List<Preset> presets = presetService.listAllPresets(new ConfigurationPresetFiltering().setRelatedBaseclass(Collections.singletonList(baseclass)), null);
            for (Preset preset : presets) {
                logger.info("preset " + preset.getName() + "(" + preset.getId() + ") was attached to permission group " + permissionGroup.getName() + "(" + permissionGroup.getId() + ") , will attach ui fields as well");

                List<UiField> fields = listAllUiFields(new UiFieldFiltering().setPresets(Collections.singletonList(preset)), adminSecurityContextBase);
                for (UiField field : fields) {
                    PermissionGroupToBaseclassCreate createPermissionGroupLinkRequest = new PermissionGroupToBaseclassCreate()
                            .setPermissionGroup(permissionGroup)
                            .setBaseclass(field.getSecurity());
                    permissionGroupToBaseclassService.createPermissionGroupToBaseclass(createPermissionGroupLinkRequest, adminSecurityContextBase);


                }

            }

        }


    }


    public List<Preset> getPreferredPresets(
            PreferedPresetRequest preferedPresetRequest,
            SecurityContextBase securityContext) {
        return presetToEntityService.getPreferredPresets(preferedPresetRequest,
                securityContext);
    }


    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return uiFieldRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return uiFieldRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return uiFieldRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return uiFieldRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return uiFieldRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return uiFieldRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return uiFieldRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        uiFieldRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        uiFieldRepository.massMerge(toMerge);
    }

    public UiField deleteUIField(UiField uiField, SecurityContextBase securityContext) {
        uiField.setSoftDelete(true);
        merge(uiField);
        return uiField;
    }

    public void validateCreate(UiFieldCreate uiFieldCreate, SecurityContextBase securityContext) {
        validate(uiFieldCreate,securityContext);
        if(uiFieldCreate.getPreset()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"preset must be provided");
        }
    }
}
