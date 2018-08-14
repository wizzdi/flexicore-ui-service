package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.ui.container.request.*;
import com.flexicore.ui.data.UiFieldRepository;
import com.flexicore.ui.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@PluginInfo(version = 1)

public class UiFieldService implements ServicePlugin {

    private AtomicBoolean init = new AtomicBoolean(false);
    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private UiFieldRepository uiFieldRepository;

    @Inject
    private BaselinkService baselinkService;


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
        UiField uiField = UiField.s().CreateUnchecked(createUiField.getName(), securityContext.getUser());
        uiField.Init();
        updateUiFieldNoMerge(createUiField, uiField);
        return uiField;
    }

    public boolean updatePresetNoMerge(CreatePreset createPreset, Preset preset) {
        boolean update = false;
        if (createPreset.getName() != null && !createPreset.getName().equals(preset.getName())) {
            update = true;
            preset.setName(createPreset.getName());
        }

        if (createPreset.getDescription() != null && (preset.getDescription() == null || !createPreset.getDescription().equals(preset.getDescription()))) {
            update = true;
            preset.setDescription(createPreset.getDescription());
        }
        return update;
    }

    public Preset updatePreset(UpdatePreset updatePreset, SecurityContext securityContext) {
        if (updatePresetNoMerge(updatePreset, updatePreset.getPreset())) {
            uiFieldRepository.merge(updatePreset.getPreset());
        }
        return updatePreset.getPreset();

    }

    public Preset createPreset(CreatePreset createPreset, SecurityContext securityContext) {
        Preset preset = Preset.s().CreateUnchecked(createPreset.getName(), securityContext.getUser());
        preset.Init();
        updatePresetNoMerge(createPreset, preset);
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
            existing = PresetToUser.s().CreateUnchecked("link", securityContext.getUser());
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
            existing = PresetToRole.s().CreateUnchecked("link", securityContext.getUser());
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
            existing = PresetToTenant.s().CreateUnchecked("link", securityContext.getUser());
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
}
