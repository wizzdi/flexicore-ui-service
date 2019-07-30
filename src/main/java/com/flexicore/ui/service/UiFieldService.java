package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.ui.container.request.*;
import com.flexicore.ui.data.UiFieldRepository;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.PresetToRoleFilter;
import com.flexicore.ui.request.PresetToTenantFilter;
import com.flexicore.ui.request.PresetToUserFilter;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)

public class UiFieldService implements ServicePlugin {

    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private UiFieldRepository uiFieldRepository;

    @Inject
    private BaselinkService baselinkService;

    @Inject
    @PluginInfo(version = 1)
    private PresetService presetService;


    public UiField updateUiField(UpdateUiField updateUiField, SecurityContext securityContext) {
        if (updateUiFieldNoMerge(updateUiField, updateUiField.getUiField())) {
            uiFieldRepository.merge(updateUiField.getUiField());
        }
        return updateUiField.getUiField();
    }

    public boolean updateUiFieldNoMerge(CreateUiField updateUiField, UiField uiField) {
        boolean update = false;
        if (updateUiField.getVisible() != null && updateUiField.getVisible() != uiField.isVisible()) {
            update = true;
            uiField.setVisible(updateUiField.getVisible());
        }

        if (updateUiField.getPriority() != null && updateUiField.getPriority() != uiField.getPriority()) {
            update = true;
            uiField.setPriority(updateUiField.getPriority());
        }

        if (updateUiField.getDescription() != null && (uiField.getDescription() == null || !updateUiField.getDescription().equals(uiField.getDescription()))) {
            update = true;
            uiField.setDescription(updateUiField.getDescription());
        }

        if (updateUiField.getName() != null && !updateUiField.getName().equals(uiField.getName())) {
            update = true;
            uiField.setName(updateUiField.getName());
        }

        if (updateUiField.getSortable() != null && updateUiField.getSortable() != uiField.isSortable()) {
            update = true;
            uiField.setSortable(updateUiField.getSortable());
        }

        if (updateUiField.getFilterable() != null && updateUiField.getFilterable() != uiField.isFilterable()) {
            update = true;
            uiField.setFilterable(updateUiField.getFilterable());
        }

        if (updateUiField.getCategory() != null && (uiField.getCategory() == null || !updateUiField.getCategory().getId().equals(uiField.getCategory().getId()))) {
            update = true;
            uiField.setCategory(updateUiField.getCategory());
        }

        if (updateUiField.getPreset() != null && (uiField.getPreset() == null || !updateUiField.getPreset().getId().equals(uiField.getPreset().getId()))) {
            update = true;
            uiField.setPreset(updateUiField.getPreset());
        }

        if (updateUiField.getDisplayName() != null && !updateUiField.getDisplayName().equals(uiField.getDisplayName())) {
            update = true;
            uiField.setDisplayName(updateUiField.getDisplayName());
        }
        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return uiFieldRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public List<UiField> listAllUiFields(UiFieldFiltering uiFieldFiltering, SecurityContext securityContext) {
        QueryInformationHolder<UiField> queryInformationHolder = new QueryInformationHolder<>(uiFieldFiltering, UiField.class, securityContext);
        return uiFieldRepository.getAllFiltered(queryInformationHolder);
    }


    public UiField createUiField(CreateUiField createUiField, SecurityContext securityContext) {
        UiField uiField = createUiFieldNoMerge(createUiField, securityContext);
        uiFieldRepository.merge(uiField);
        return uiField;

    }

    private UiField createUiFieldNoMerge(CreateUiField createUiField, SecurityContext securityContext) {
        UiField uiField = UiField.s().CreateUnchecked(createUiField.getName(), securityContext);
        uiField.Init();
        updateUiFieldNoMerge(createUiField, uiField);
        return uiField;
    }

    public boolean updateGridPresetNoMerge(CreateGridPreset createPreset, GridPreset preset) {
        boolean update = presetService.updatePresetNoMerge(createPreset,preset);

        return update;
    }

    public GridPreset updatePreset(UpdateGridPreset updatePreset, SecurityContext securityContext) {
        if (updateGridPresetNoMerge(updatePreset, updatePreset.getPreset())) {
            uiFieldRepository.merge(updatePreset.getPreset());
        }
        return updatePreset.getPreset();

    }

    public GridPreset createGridPreset(CreateGridPreset createPreset, SecurityContext securityContext) {
        GridPreset preset = GridPreset.s().CreateUnchecked(createPreset.getName(), securityContext);
        preset.Init();
        updateGridPresetNoMerge(createPreset, preset);
        List<Object> toMerge = new ArrayList<>();
        toMerge.add(preset);
        for (CreateUiField createUiField : createPreset.getUiFields()) {
            UiField uiField = createUiFieldNoMerge(createUiField, securityContext);
            toMerge.add(uiField);
        }
        uiFieldRepository.massMerge(toMerge);
        return preset;

    }



    public void validate(PresetToUserCreate linkPresetToUser,  SecurityContext securityContext) {
        String presetId = linkPresetToUser.getPresetId();
        Preset preset = presetId != null ? getByIdOrNull(presetId, Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + presetId);
        }
        linkPresetToUser.setPreset(preset);
        String userId = linkPresetToUser.getUserId();
        User user = userId != null ? getByIdOrNull(userId, User.class, null, securityContext) : null;
        if (user == null) {
            throw new BadRequestException("no user with id " + userId);
        }
        linkPresetToUser.setUser(user);
    }


    public void validate(PresetToRoleCreate presetToRoleCreate, SecurityContext securityContext) {
        String presetId = presetToRoleCreate.getPresetId();
        Preset preset = presetId != null ? getByIdOrNull(presetId, Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + presetId);
        }
        presetToRoleCreate.setPreset(preset);
        String roleId = presetToRoleCreate.getRoleId();
        Role role = roleId != null ? getByIdOrNull(roleId, Role.class, null, securityContext) : null;
        if (role == null) {
            throw new BadRequestException("no role with id " + roleId);
        }
        presetToRoleCreate.setRole(role);
    }


    public void validate(PresetToTenantCreate presetToTenantCreate, SecurityContext securityContext) {
        String presetId = presetToTenantCreate.getPresetId();
        Preset preset = presetId != null ? getByIdOrNull(presetId, Preset.class, null, securityContext) : null;
        if (preset == null) {
            throw new BadRequestException("no preset with id " + presetId);
        }
        presetToTenantCreate.setPreset(preset);
        String tenantId = presetToTenantCreate.getTenantId();
        Tenant tenant = tenantId != null ? getByIdOrNull(tenantId, Tenant.class, null, securityContext) : null;
        if (tenant == null) {
            throw new BadRequestException("no tenant with id " + tenantId);
        }
        presetToTenantCreate.setTenant(tenant);
    }

    public PresetToRole createPresetToRole(PresetToRoleCreate presetToRoleCreate, SecurityContext securityContext) {
        PresetToRole toRet;
        List<PresetToRole> existing=listAllPresetToRole(new PresetToRoleFilter().setRoles(Collections.singletonList(presetToRoleCreate.getRole())).setPresets(Collections.singletonList(presetToRoleCreate.getPreset())),securityContext);
        if(existing.isEmpty()){
            toRet=createPresetToRoleNoMerge(presetToRoleCreate,securityContext);
            uiFieldRepository.merge(toRet);
        }
        else{
            toRet=existing.get(0);
        }

        return toRet;
    }

    private PresetToRole createPresetToRoleNoMerge(PresetToRoleCreate presetToRoleCreate, SecurityContext securityContext) {
        PresetToRole presetToRole=PresetToRole.s().CreateUnchecked("presetToRole",securityContext);
        presetToRole.Init(presetToRoleCreate.getPreset(),presetToRoleCreate.getRole());
        updatePresetToRoleNoMerge(presetToRoleCreate,presetToRole);
        return presetToRole;
    }


    public PresetToTenant createPresetToTenant(PresetToTenantCreate presetToTenantCreate, SecurityContext securityContext) {
        PresetToTenant toRet;
        List<PresetToTenant> existing=listAllPresetToTenant(new PresetToTenantFilter().setTenants(Collections.singletonList(presetToTenantCreate.getTenant())).setPresets(Collections.singletonList(presetToTenantCreate.getPreset())),securityContext);
        if(existing.isEmpty()){
            toRet=createPresetToTenantNoMerge(presetToTenantCreate,securityContext);
            uiFieldRepository.merge(toRet);
        }
        else{
            toRet=existing.get(0);
        }

        return toRet;
    }

    private PresetToTenant createPresetToTenantNoMerge(PresetToTenantCreate presetToTenantCreate, SecurityContext securityContext) {
        PresetToTenant presetToTenant=PresetToTenant.s().CreateUnchecked("presetToTenant",securityContext);
        presetToTenant.Init(presetToTenantCreate.getPreset(),presetToTenantCreate.getTenant());
        updatePresetToTenantNoMerge(presetToTenantCreate,presetToTenant);
        return presetToTenant;
    }



    public PresetToUser createPresetToUser(PresetToUserCreate presetToUserCreate, SecurityContext securityContext) {
        PresetToUser toRet;
        List<PresetToUser> existing=listAllPresetToUser(new PresetToUserFilter().setUsers(Collections.singletonList(presetToUserCreate.getUser())).setPresets(Collections.singletonList(presetToUserCreate.getPreset())),securityContext);
        if(existing.isEmpty()){
            toRet=createPresetToUserNoMerge(presetToUserCreate,securityContext);
            uiFieldRepository.merge(toRet);
        }
        else{
            toRet=existing.get(0);
        }

        return toRet;
    }

    private PresetToUser createPresetToUserNoMerge(PresetToUserCreate presetToUserCreate, SecurityContext securityContext) {
        PresetToUser presetToUser=PresetToUser.s().CreateUnchecked("presetToUser",securityContext);
        presetToUser.Init(presetToUserCreate.getPreset(),presetToUserCreate.getUser());
        updatePresetToUserNoMerge(presetToUserCreate,presetToUser);
        return presetToUser;
    }



    public boolean  updatePresetToTenantNoMerge(PresetToTenantCreate presetToTenantCreate,PresetToTenant presetToTenant) {
        boolean update = false;
        if (presetToTenantCreate.getPriority() != null && presetToTenantCreate.getPriority() != presetToTenant.getPriority()) {
            presetToTenant.setPriority(presetToTenantCreate.getPriority());
            update = true;
        }
        if (presetToTenantCreate.getEnabled() != null && presetToTenantCreate.getEnabled() != presetToTenant.isEnabled()) {
            presetToTenant.setEnabled(presetToTenantCreate.getEnabled());
            update = true;
        }

        if (presetToTenantCreate.getPreset() != null && (presetToTenant.getLeftside()==null||!presetToTenantCreate.getPreset().getId().equals(presetToTenant.getLeftside().getId()))) {
            presetToTenant.setLeftside(presetToTenantCreate.getPreset());
            update = true;
        }
        if (presetToTenantCreate.getTenant() != null && (presetToTenant.getRightside()==null||!presetToTenantCreate.getTenant().getId().equals(presetToTenant.getRightside().getId()))) {
            presetToTenant.setRightside(presetToTenantCreate.getTenant());
            update = true;
        }
        return update;
    }


    public PresetToTenant updatePresetToTenant(PresetToTenantUpdate updateLinkPresetToTenant, SecurityContext securityContext){
        if(updatePresetToTenantNoMerge(updateLinkPresetToTenant,updateLinkPresetToTenant.getPresetToTenant())){
            uiFieldRepository.merge(updateLinkPresetToTenant.getPresetToTenant());
        }
        return updateLinkPresetToTenant.getPresetToTenant();
    }

    public PresetToUser updatePresetToUser(PresetToUserUpdate updateLinkPresetToUser, SecurityContext securityContext){
        if(updatePresetToUserNoMerge(updateLinkPresetToUser, updateLinkPresetToUser.getPresetToUser())){
            uiFieldRepository.merge(updateLinkPresetToUser.getPresetToUser());
        }
        return updateLinkPresetToUser.getPresetToUser();
    }

    public boolean  updatePresetToUserNoMerge(PresetToUserCreate presetToUserCreate,PresetToUser presetToUser) {
        boolean update = false;
        if (presetToUserCreate.getPriority() != null && presetToUserCreate.getPriority() != presetToUser.getPriority()) {
            presetToUser.setPriority(presetToUserCreate.getPriority());
            update = true;
        }
        if (presetToUserCreate.getEnabled() != null && presetToUserCreate.getEnabled() != presetToUser.isEnabled()) {
            presetToUser.setEnabled(presetToUserCreate.getEnabled());
            update = true;
        }

        if (presetToUserCreate.getPreset() != null && (presetToUser.getLeftside()==null||!presetToUserCreate.getPreset().getId().equals(presetToUser.getLeftside().getId()))) {
            presetToUser.setLeftside(presetToUserCreate.getPreset());
            update = true;
        }
        if (presetToUserCreate.getUser() != null && (presetToUser.getRightside()==null||!presetToUserCreate.getUser().getId().equals(presetToUser.getRightside().getId()))) {
            presetToUser.setRightside(presetToUserCreate.getUser());
            update = true;
        }
        return update;
    }

    public PresetToRole updatePresetToRole(PresetToRoleUpdate updateLinkPresetToRole, SecurityContext securityContext){
        if(updatePresetToRoleNoMerge(updateLinkPresetToRole,updateLinkPresetToRole.getPresetToRole())){
            uiFieldRepository.merge(updateLinkPresetToRole.getPresetToRole());
        }
        return updateLinkPresetToRole.getPresetToRole();
    }

    public boolean  updatePresetToRoleNoMerge(PresetToRoleCreate presetToRoleCreate,PresetToRole presetToRole) {
        boolean update = false;
        if (presetToRoleCreate.getPriority() != null && presetToRoleCreate.getPriority() != presetToRole.getPriority()) {
            presetToRole.setPriority(presetToRoleCreate.getPriority());
            update = true;
        }
        if (presetToRoleCreate.getEnabled() != null && presetToRoleCreate.getEnabled() != presetToRole.isEnabled()) {
            presetToRole.setEnabled(presetToRoleCreate.getEnabled());
            update = true;
        }

        if (presetToRoleCreate.getPreset() != null && (presetToRole.getLeftside()==null||!presetToRoleCreate.getPreset().getId().equals(presetToRole.getLeftside().getId()))) {
            presetToRole.setLeftside(presetToRoleCreate.getPreset());
            update = true;
        }
        if (presetToRoleCreate.getRole() != null && (presetToRole.getRightside()==null||!presetToRoleCreate.getRole().getId().equals(presetToRole.getRightside().getId()))) {
            presetToRole.setRightside(presetToRoleCreate.getRole());
            update = true;
        }
        return update;
    }


    public void validate(PresetToRoleFilter presetToRoleFilter, SecurityContext securityContext) {
        Set<String> roleIds=presetToRoleFilter.getRoleIds();
        Map<String, Role> map=roleIds.isEmpty()?new HashMap<>():uiFieldRepository.listByIds(Role.class,roleIds,securityContext).parallelStream().collect(Collectors.toMap(f->f.getId(), f->f));
        roleIds.removeAll(map.keySet());
        if(!roleIds.isEmpty()){
            throw new BadRequestException("No Roles with ids "+roleIds);
        }
        presetToRoleFilter.setRoles(new ArrayList<>(map.values()));

    }


    public PaginationResponse<PresetToRole> getAllPresetToRole(PresetToRoleFilter presetToRoleFilter, SecurityContext securityContext) {
        List<PresetToRole> list=listAllPresetToRole(presetToRoleFilter,securityContext);
        long count=uiFieldRepository.countAllPresetToRoles(presetToRoleFilter,securityContext);
        return new PaginationResponse<>(list,presetToRoleFilter,count);
    }

    private List<PresetToRole> listAllPresetToRole(PresetToRoleFilter presetToRoleFilter, SecurityContext securityContext) {
        return uiFieldRepository.listAllPresetToRoles(presetToRoleFilter,securityContext);
    }


    public void validate(PresetToTenantFilter presetToTenantFilter, SecurityContext securityContext) {
        Set<String> tenantIds=presetToTenantFilter.getTenantIdsForPreset();
        Map<String, Tenant> map=tenantIds.isEmpty()?new HashMap<>():uiFieldRepository.listByIds(Tenant.class,tenantIds,securityContext).parallelStream().collect(Collectors.toMap(f->f.getId(), f->f));
        tenantIds.removeAll(map.keySet());
        if(!tenantIds.isEmpty()){
            throw new BadRequestException("No Tenants with ids "+tenantIds);
        }
        presetToTenantFilter.setTenants(new ArrayList<>(map.values()));

    }


    public PaginationResponse<PresetToTenant> getAllPresetToTenant(PresetToTenantFilter presetToTenantFilter, SecurityContext securityContext) {
        List<PresetToTenant> list=listAllPresetToTenant(presetToTenantFilter,securityContext);
        long count=uiFieldRepository.countAllPresetToTenants(presetToTenantFilter,securityContext);
        return new PaginationResponse<>(list,presetToTenantFilter,count);
    }

    private List<PresetToTenant> listAllPresetToTenant(PresetToTenantFilter presetToTenantFilter, SecurityContext securityContext) {
        return uiFieldRepository.listAllPresetToTenants(presetToTenantFilter,securityContext);
    }


    public void validate(PresetToUserFilter presetToUserFilter, SecurityContext securityContext) {
        Set<String> userIds=presetToUserFilter.getUserIds();
        Map<String, User> map=userIds.isEmpty()?new HashMap<>():uiFieldRepository.listByIds(User.class,userIds,securityContext).parallelStream().collect(Collectors.toMap(f->f.getId(), f->f));
        userIds.removeAll(map.keySet());
        if(!userIds.isEmpty()){
            throw new BadRequestException("No Users with ids "+userIds);
        }
        presetToUserFilter.setUsers(new ArrayList<>(map.values()));

    }


    public PaginationResponse<PresetToUser> getAllPresetToUser(PresetToUserFilter presetToUserFilter, SecurityContext securityContext) {
        List<PresetToUser> list=listAllPresetToUser(presetToUserFilter,securityContext);
        long count=uiFieldRepository.countAllPresetToUsers(presetToUserFilter,securityContext);
        return new PaginationResponse<>(list,presetToUserFilter,count);
    }

    private List<PresetToUser> listAllPresetToUser(PresetToUserFilter presetToUserFilter, SecurityContext securityContext) {
        return uiFieldRepository.listAllPresetToUsers(presetToUserFilter,securityContext);
    }
}
