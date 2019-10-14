package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.request.*;
import com.flexicore.ui.data.GridPresetRepository;
import com.flexicore.ui.model.GridPreset;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PluginInfo(version = 1)
public class GridPresetService implements ServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private PresetService presetService;

    @Inject
    @PluginInfo(version = 1)
    private GridPresetRepository gridPresetRepository;

    @Inject
    @PluginInfo(version = 1)
    private UiFieldService uiFieldService;

    public PaginationResponse<GridPreset> getAllGridPresets(GridPresetFiltering gridPresetFiltering, SecurityContext securityContext) {
        List<GridPreset> list = listAllGridPresets(gridPresetFiltering, securityContext);
        long count = gridPresetRepository.countAllGridPresets(gridPresetFiltering, securityContext);
        return new PaginationResponse<>(list, gridPresetFiltering, count);
    }

    public List<GridPreset> listAllGridPresets(GridPresetFiltering gridPresetFiltering, SecurityContext securityContext) {
        return gridPresetRepository.listAllGridPresets(gridPresetFiltering, securityContext);
    }

    public boolean updateGridPresetNoMerge(GridPresetCreate createPreset, GridPreset preset) {
        boolean update = presetService.updatePresetNoMerge(createPreset, preset);
        if(createPreset.getRelatedClassCanonicalName()!=null &&!createPreset.getRelatedClassCanonicalName().equals(preset.getRelatedClassCanonicalName())){
            preset.setRelatedClassCanonicalName(createPreset.getRelatedClassCanonicalName());
            update=true;
        }

        if(createPreset.getDynamicExecution()!=null &&(preset.getDynamicExecution()==null||!createPreset.getDynamicExecution().getId().equals(preset.getDynamicExecution().getId()))){
            preset.setDynamicExecution(createPreset.getDynamicExecution());
            update=true;
        }


        return update;
    }

    public GridPreset updateGridPreset(GridPresetUpdate updatePreset, SecurityContext securityContext) {
        if (updateGridPresetNoMerge(updatePreset, updatePreset.getPreset())) {
            gridPresetRepository.merge(updatePreset.getPreset());
        }
        return updatePreset.getPreset();

    }

    public GridPreset createGridPreset(GridPresetCreate createPreset, SecurityContext securityContext) {
        GridPreset preset = createGridPresetNoMerge(createPreset, securityContext);
        gridPresetRepository.merge(preset);
        return preset;

    }

    public GridPreset createGridPresetNoMerge(GridPresetCreate createPreset, SecurityContext securityContext) {
        GridPreset preset = GridPreset.s().CreateUnchecked(createPreset.getName(), securityContext);
        preset.Init();
        updateGridPresetNoMerge(createPreset, preset);
        return preset;
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return gridPresetRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public void validateCopy(GridPresetCopy gridPresetCopy,SecurityContext securityContext){
        validate(gridPresetCopy,securityContext);
        String gridPresetId=gridPresetCopy.getId();
        GridPreset gridPreset=gridPresetId!=null?getByIdOrNull(gridPresetId,GridPreset.class,null,securityContext):null;
        if(gridPreset==null){
            throw new BadRequestException("No Grid Preset With id "+gridPresetId);
        }
        gridPresetCopy.setPreset(gridPreset);
    }

    public void validate(GridPresetCreate createGridPreset, SecurityContext securityContext) {
        String dynamicExecutionId = createGridPreset.getDynamicExecutionId();
        DynamicExecution dynamicExecution = dynamicExecutionId == null ? null : getByIdOrNull(dynamicExecutionId, DynamicExecution.class, null, securityContext);
        if (dynamicExecution == null && dynamicExecutionId != null) {
            throw new BadRequestException("No Dynamic Execution with id " + dynamicExecutionId);
        }
        createGridPreset.setDynamicExecution(dynamicExecution);
    }

    public GridPreset copyGridPreset(GridPresetCopy gridPresetCopy, SecurityContext securityContext) {
        List<Object> toMerge=new ArrayList<>();
        GridPresetCreate gridPresetCreate=getCreateContainer(gridPresetCopy.getPreset());
        GridPreset gridPreset=createGridPresetNoMerge(gridPresetCreate,securityContext);
        updateGridPresetNoMerge(gridPresetCopy,gridPreset);
        toMerge.add(gridPreset);
        List<UiField> uiFields=uiFieldService.listAllUiFields(new UiFieldFiltering().setGridPresets(Collections.singletonList(gridPresetCopy.getPreset())),securityContext);
        for (UiField uiField : uiFields) {
            UiFieldCreate uiFieldCreate=uiFieldService.getUIFieldCreate(uiField);
            uiFieldCreate.setGridPreset(gridPreset);
            UiField uiFieldNoMerge = uiFieldService.createUiFieldNoMerge(uiFieldCreate, securityContext);
            toMerge.add(uiFieldNoMerge);
        }
        gridPresetRepository.massMerge(toMerge);
        return gridPreset;

    }

    private GridPresetCreate getCreateContainer(GridPreset preset) {
        return new GridPresetCreate()
                .setDynamicExecution(preset.getDynamicExecution())
                .setRelatedClassCanonicalName(preset.getRelatedClassCanonicalName())
                .setDescription(preset.getDescription())
                .setName(preset.getName());
    }
}