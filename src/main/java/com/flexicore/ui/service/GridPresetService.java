package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CreatePermissionGroupLinkRequest;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.events.BaseclassCreated;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.PermissionGroupService;
import com.flexicore.service.SecurityService;
import com.flexicore.ui.data.GridPresetRepository;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution_;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class GridPresetService implements ServicePlugin {

	private static final Logger logger= LoggerFactory.getLogger(GridPresetService.class);

	@PluginInfo(version = 1)
	@Autowired
	private PresetService presetService;

	@PluginInfo(version = 1)
	@Autowired
	private GridPresetRepository gridPresetRepository;

	@PluginInfo(version = 1)
	@Autowired
	private UiFieldService uiFieldService;

	@Autowired
	private SecurityService securityService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private DynamicExecutionService dynamicExecutionService;
	private SecurityContext adminSecurityContext;

	public PaginationResponse<GridPreset> getAllGridPresets(
			GridPresetFiltering gridPresetFiltering,
			SecurityContext securityContext) {
		List<GridPreset> list = listAllGridPresets(gridPresetFiltering,
				securityContext);
		long count = gridPresetRepository.countAllGridPresets(
				gridPresetFiltering, securityContext);
		return new PaginationResponse<>(list, gridPresetFiltering, count);
	}

	public List<GridPreset> listAllGridPresets(
			GridPresetFiltering gridPresetFiltering,
			SecurityContext securityContext) {
		return gridPresetRepository.listAllGridPresets(gridPresetFiltering,
				securityContext);
	}

	public boolean updateGridPresetNoMerge(GridPresetCreate createPreset,
			GridPreset preset) {
		boolean update = presetService
				.updatePresetNoMerge(createPreset, preset);
		if (createPreset.getRelatedClassCanonicalName() != null && !createPreset.getRelatedClassCanonicalName().equals(preset.getRelatedClassCanonicalName())) {
			preset.setRelatedClassCanonicalName(createPreset.getRelatedClassCanonicalName());
			update = true;
		}

		if (createPreset.getLatMapping() != null && !createPreset.getLatMapping().equals(preset.getLatMapping())) {
			preset.setLatMapping(createPreset.getLatMapping());
			update = true;
		}

		if (createPreset.getLonMapping() != null && !createPreset.getLonMapping().equals(preset.getLonMapping())) {
			preset.setLonMapping(createPreset.getLonMapping());
			update = true;
		}

		if (createPreset.getDynamicExecution() != null && (preset.getDynamicExecution() == null || !createPreset.getDynamicExecution().getId().equals(preset.getDynamicExecution().getId()))) {
			preset.setDynamicExecution(createPreset.getDynamicExecution());
			update = true;
		}

		return update;
	}

	public GridPreset updateGridPreset(GridPresetUpdate updatePreset,
			SecurityContext securityContext) {
		if (updateGridPresetNoMerge(updatePreset, updatePreset.getPreset())) {
			gridPresetRepository.merge(updatePreset.getPreset());
		}
		return updatePreset.getPreset();

	}

	public GridPreset createGridPreset(GridPresetCreate createPreset,
			SecurityContext securityContext) {
		GridPreset preset = createGridPresetNoMerge(createPreset,
				securityContext);
		gridPresetRepository.merge(preset);
		return preset;

	}

	public GridPreset createGridPresetNoMerge(GridPresetCreate createPreset,
			SecurityContext securityContext) {
		GridPreset preset = new GridPreset(
				createPreset.getName(), securityContext);
		updateGridPresetNoMerge(createPreset, preset);
		return preset;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return gridPresetRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public void validateCopy(GridPresetCopy gridPresetCopy,
			SecurityContext securityContext) {
		validate(gridPresetCopy, securityContext);
		String gridPresetId = gridPresetCopy.getId();
		GridPreset gridPreset = gridPresetId != null ? getByIdOrNull(
				gridPresetId, GridPreset.class, null, securityContext) : null;
		if (gridPreset == null) {
			throw new BadRequestException("No Grid Preset With id "
					+ gridPresetId);
		}
		gridPresetCopy.setPreset(gridPreset);
	}

	public void validate(GridPresetCreate createGridPreset,
			SecurityContext securityContext) {
		String dynamicExecutionId = createGridPreset.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId == null
				? null
				: dynamicExecutionService.getByIdOrNull(dynamicExecutionId, DynamicExecution.class, SecuredBasic_.security, securityContext);
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new BadRequestException("No Dynamic Execution with id "
					+ dynamicExecutionId);
		}
		createGridPreset.setDynamicExecution(dynamicExecution);
	}

	public GridPreset copyGridPreset(GridPresetCopy gridPresetCopy,
			SecurityContext securityContext) {
		List<Object> toMerge = new ArrayList<>();
		GridPresetCreate gridPresetCreate = getCreateContainer(gridPresetCopy
				.getPreset());
		GridPreset gridPreset = createGridPresetNoMerge(gridPresetCreate,
				securityContext);
		updateGridPresetNoMerge(gridPresetCopy, gridPreset);
		toMerge.add(gridPreset);
		List<UiField> uiFields = uiFieldService.listAllUiFields(
				new UiFieldFiltering().setPresets(Collections
						.singletonList(gridPresetCopy.getPreset())),
				securityContext);
		for (UiField uiField : uiFields) {
			UiFieldCreate uiFieldCreate = uiFieldService
					.getUIFieldCreate(uiField);
			uiFieldCreate.setPreset(gridPreset);
			UiField uiFieldNoMerge = uiFieldService.createUiFieldNoMerge(
					uiFieldCreate, securityContext);
			toMerge.add(uiFieldNoMerge);
		}
		gridPresetRepository.massMerge(toMerge);
		return gridPreset;

	}

	@EventListener
	@Async
	public void handlePresetPermissionGroupCreated(BaseclassCreated<PermissionGroupToBaseclass> baseclassCreated){
		PermissionGroupToBaseclass permissionGroupToBaseclass = baseclassCreated.getBaseclass();
		PermissionGroup permissionGroup=permissionGroupToBaseclass.getLeftside();
		if(permissionGroupToBaseclass.getRightside() instanceof GridPreset){
			SecurityContext securityContext=getAdminSecurityContext();
			GridPreset preset= (GridPreset) permissionGroupToBaseclass.getRightside();
			if(preset.getDynamicExecution()!=null){
				logger.info("grid preset "+preset.getName() +"("+preset.getId()+") was attached to permission group "+permissionGroup.getName()+"("+permissionGroup.getId()+") , will attach dynamic execution");
				if(preset.getDynamicExecution().getSecurity()!=null){
					CreatePermissionGroupLinkRequest createPermissionGroupLinkRequest = new CreatePermissionGroupLinkRequest()
							.setPermissionGroups(Collections.singletonList(permissionGroup))
							.setBaseclasses(Collections.singletonList(preset.getDynamicExecution().getSecurity()));
					permissionGroupService.connectPermissionGroupsToBaseclasses(createPermissionGroupLinkRequest,securityContext);
				}


			}

		}

	}

	private SecurityContext getAdminSecurityContext() {
		if(adminSecurityContext==null){
			adminSecurityContext=securityService.getAdminUserSecurityContext();
		}
		return adminSecurityContext;
	}


	private GridPresetCreate getCreateContainer(GridPreset preset) {
		return new GridPresetCreate()
				.setDynamicExecution(preset.getDynamicExecution())
				.setRelatedClassCanonicalName(
						preset.getRelatedClassCanonicalName())
				.setDescription(preset.getDescription())
				.setName(preset.getName());
	}
}
