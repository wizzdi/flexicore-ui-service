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
import com.flexicore.ui.request.PresetLinkFilter;
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

    public boolean  updatePresetToUserNoMerge(UpdateLinkPresetToUser linkPresetToUser) {
        PresetToUser presetToUser=linkPresetToUser.getPresetToUser();
        boolean update = false;
        if (linkPresetToUser.getPriority() != null && linkPresetToUser.getPriority() != presetToUser.getPriority()) {
            update = true;
            presetToUser.setPriority(linkPresetToUser.getPriority());
        }

        if (linkPresetToUser.getEnabled() != null && linkPresetToUser.getEnabled() != presetToUser.isEnabled()) {
            update = true;
            presetToUser.setEnabled(linkPresetToUser.getEnabled());
        }
        return update;
    }

    public PresetToUser linkPresetToUser(LinkPresetToUser linkPresetToUser, SecurityContext securityContext) {
        boolean created=false;
        PresetToUser existing = baselinkService.findBySides(PresetToUser.class, linkPresetToUser.getPreset(), linkPresetToUser.getUser());
        if (existing != null) {
            existing = PresetToUser.s().CreateUnchecked("link", securityContext);
            existing.Init();
            created=true;

        }
        if(updatePresetToUserNoMerge(new UpdateLinkPresetToUser(linkPresetToUser,existing))|created){
            uiFieldRepository.merge(existing);
        }
        return existing;


    }


    public boolean  updatePresetToRoleNoMerge(UpdateLinkPresetToRole linkPresetToUser) {
        PresetToRole presetToUser=linkPresetToUser.getPresetToRole();
        boolean update = false;
        if (linkPresetToUser.getPriority() != null && linkPresetToUser.getPriority() != presetToUser.getPriority()) {
            update = true;
            presetToUser.setPriority(linkPresetToUser.getPriority());
        }

        if (linkPresetToUser.getEnabled() != null && linkPresetToUser.getEnabled() != presetToUser.isEnabled()) {
            update = true;
            presetToUser.setEnabled(linkPresetToUser.getEnabled());
        }
        return update;
    }

    public PresetToRole linkPresetToRole(LinkPresetToRole linkPresetToRole, SecurityContext securityContext) {
        boolean created=false;
        PresetToRole existing = baselinkService.findBySides(PresetToRole.class, linkPresetToRole.getPreset(), linkPresetToRole.getRole());
        if (existing != null) {
            existing = PresetToRole.s().CreateUnchecked("link", securityContext);
            existing.Init();
            created=true;

        }
        if(updatePresetToRoleNoMerge(new UpdateLinkPresetToRole(linkPresetToRole,existing))|created){
            uiFieldRepository.merge(existing);
        }
        return existing;


    }




    public boolean updatePresetToTenantNoMerge(UpdateLinkPresetToTenant linkPresetToUser) {
        PresetToTenant presetToUser=linkPresetToUser.getPresetToTenant();
        boolean update = false;
        if (linkPresetToUser.getPriority() != null && linkPresetToUser.getPriority() != presetToUser.getPriority()) {
            update = true;
            presetToUser.setPriority(linkPresetToUser.getPriority());
        }

        if (linkPresetToUser.getEnabled() != null && linkPresetToUser.getEnabled() != presetToUser.isEnabled()) {
            update = true;
            presetToUser.setEnabled(linkPresetToUser.getEnabled());
        }
        return update;
    }

    public PresetToTenant linkPresetToTenant(LinkPresetToTenant linkPresetToTenant, SecurityContext securityContext) {
        boolean created=false;
        PresetToTenant existing = baselinkService.findBySides(PresetToTenant.class, linkPresetToTenant.getPreset(), linkPresetToTenant.getTenant());
        if (existing != null) {
            existing = PresetToTenant.s().CreateUnchecked("link", securityContext);
            existing.Init();
            created=true;

        }
        if(updatePresetToTenantNoMerge(new UpdateLinkPresetToTenant(linkPresetToTenant,existing))|created){
            uiFieldRepository.merge(existing);
        }
        return existing;


    }


    public PresetToTenant updatePresetToTenant(UpdateLinkPresetToTenant updateLinkPresetToTenant,SecurityContext securityContext){
        if(updatePresetToTenantNoMerge(updateLinkPresetToTenant)){
            uiFieldRepository.merge(updateLinkPresetToTenant.getPresetToTenant());
        }
        return updateLinkPresetToTenant.getPresetToTenant();
    }

    public PresetToUser updatePresetToUser(UpdateLinkPresetToUser updateLinkPresetToUser,SecurityContext securityContext){
        if(updatePresetToUserNoMerge(updateLinkPresetToUser)){
            uiFieldRepository.merge(updateLinkPresetToUser.getPresetToUser());
        }
        return updateLinkPresetToUser.getPresetToUser();
    }

    public PresetToRole updatePresetToRole(UpdateLinkPresetToRole updateLinkPresetToRole,SecurityContext securityContext){
        if(updatePresetToRoleNoMerge(updateLinkPresetToRole)){
            uiFieldRepository.merge(updateLinkPresetToRole.getPresetToRole());
        }
        return updateLinkPresetToRole.getPresetToRole();
    }

    public void validatePresetLink(PresetLinkFilter presetToRoleFilter, SecurityContext securityContext) {
        Set<String> presetIds=presetToRoleFilter.getPresetIds();
        Map<String,Preset> map=presetIds.isEmpty()?new HashMap<>():uiFieldRepository.listByIds(Preset.class,presetIds,securityContext).parallelStream().collect(Collectors.toMap(f->f.getId(),f->f));
        presetIds.removeAll(map.keySet());
        if(!presetIds.isEmpty()){
            throw new BadRequestException("No presets with ids "+presetIds);
        }
        presetToRoleFilter.setPresets(new ArrayList<>(map.values()));

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
