package com.flexicore.ui.service;


import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.GridPresetRepository;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.GridPreset_;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.request.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
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


@Extension
@Component
public class GridPresetService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(GridPresetService.class);

	
	@Autowired
	private PresetService presetService;

	
	@Autowired
	private GridPresetRepository gridPresetRepository;

	
	@Autowired
	private UiFieldService uiFieldService;

	@Autowired
	private PermissionGroupService permissionGroupService;
	@Autowired
	private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
	@Autowired
	private DynamicExecutionService dynamicExecutionService;
	@Autowired
	@Lazy
	private SecurityContextBase adminSecurityContextBase;
	@Autowired
	@Lazy
	@Qualifier("gridPresetClazz")
	private Clazz gridPresetClazz;

	public PaginationResponse<GridPreset> getAllGridPresets(
			GridPresetFiltering gridPresetFiltering,
			SecurityContextBase securityContext) {
		List<GridPreset> list = listAllGridPresets(gridPresetFiltering,
				securityContext);
		long count = gridPresetRepository.countAllGridPresets(
				gridPresetFiltering, securityContext);
		return new PaginationResponse<>(list, gridPresetFiltering, count);
	}

	public List<GridPreset> listAllGridPresets(
			GridPresetFiltering gridPresetFiltering,
			SecurityContextBase securityContext) {
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
			SecurityContextBase securityContext) {
		if (updateGridPresetNoMerge(updatePreset, updatePreset.getPreset())) {
			gridPresetRepository.merge(updatePreset.getPreset());
		}
		return updatePreset.getPreset();

	}

	public GridPreset createGridPreset(GridPresetCreate createPreset,
			SecurityContextBase securityContext) {
		GridPreset preset = createGridPresetNoMerge(createPreset,
				securityContext);
		gridPresetRepository.merge(preset);
		return preset;

	}

	public GridPreset createGridPresetNoMerge(GridPresetCreate createPreset,
			SecurityContextBase securityContext) {
		GridPreset preset = new GridPreset();
		preset.setId(UUID.randomUUID().toString());
		updateGridPresetNoMerge(createPreset, preset);
		BaseclassService.createSecurityObjectNoMerge(preset,securityContext);
		return preset;
	}


	public void validateCopy(GridPresetCopy gridPresetCopy,
			SecurityContextBase securityContext) {
		validate(gridPresetCopy, securityContext);
		String gridPresetId = gridPresetCopy.getId();
		GridPreset gridPreset = gridPresetId != null ? getByIdOrNull(gridPresetId, GridPreset.class, GridPreset_.security, securityContext) : null;
		if (gridPreset == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Grid Preset With id " + gridPresetId);
		}
		gridPresetCopy.setPreset(gridPreset);
	}

	public void validate(GridPresetCreate createGridPreset,
			SecurityContextBase securityContext) {
		String dynamicExecutionId = createGridPreset.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId == null ? null : dynamicExecutionService.getByIdOrNull(dynamicExecutionId, DynamicExecution.class, SecuredBasic_.security, securityContext);
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Dynamic Execution with id " + dynamicExecutionId);
		}
		createGridPreset.setDynamicExecution(dynamicExecution);
	}

	public GridPreset copyGridPreset(GridPresetCopy gridPresetCopy,
			SecurityContextBase securityContext) {
		List<Object> toMerge = new ArrayList<>();
		GridPresetCreate gridPresetCreate = getCreateContainer(gridPresetCopy.getPreset());
		GridPreset gridPreset = createGridPresetNoMerge(gridPresetCreate, securityContext);
		updateGridPresetNoMerge(gridPresetCopy, gridPreset);
		toMerge.add(gridPreset);
		List<UiField> uiFields = uiFieldService.listAllUiFields(new UiFieldFiltering().setPresets(Collections.singletonList(gridPresetCopy.getPreset())), securityContext);
		for (UiField uiField : uiFields) {
			UiFieldCreate uiFieldCreate = uiFieldService.getUIFieldCreate(uiField);
			uiFieldCreate.setPreset(gridPreset);
			UiField uiFieldNoMerge = uiFieldService.createUiFieldNoMerge(uiFieldCreate, securityContext);
			toMerge.add(uiFieldNoMerge);
		}
		gridPresetRepository.massMerge(toMerge);
		return gridPreset;

	}

	@EventListener
	@Async
	public void handlePresetPermissionGroupCreated(BasicCreated<PermissionGroupToBaseclass> baseclassCreated){
		PermissionGroupToBaseclass permissionGroupToBaseclass = baseclassCreated.getBaseclass();
		PermissionGroup permissionGroup=permissionGroupToBaseclass.getLeftside();
		if(permissionGroupToBaseclass.getRightside().getClazz().getId().equals(gridPresetClazz.getId())){
			SecurityContextBase securityContext=adminSecurityContextBase;
			Baseclass baseclass= permissionGroupToBaseclass.getRightside();
			List<GridPreset> presets=listAllGridPresets(new GridPresetFiltering().setRelatedBaseclass(Collections.singletonList(baseclass)),null);
			for (GridPreset preset : presets) {
				if(preset.getDynamicExecution()!=null){
					logger.info("grid preset "+preset.getName() +"("+preset.getId()+") was attached to permission group "+permissionGroup.getName()+"("+permissionGroup.getId()+") , will attach dynamic execution");
					if(preset.getDynamicExecution().getSecurity()!=null){
						PermissionGroupToBaseclassCreate createPermissionGroupLinkRequest = new PermissionGroupToBaseclassCreate()
								.setPermissionGroup(permissionGroup)
								.setBaseclass(preset.getDynamicExecution().getSecurity());
						permissionGroupToBaseclassService.createPermissionGroupToBaseclass(createPermissionGroupLinkRequest,securityContext);
					}


				}
			}


		}

	}


	private GridPresetCreate getCreateContainer(GridPreset preset) {
		return new GridPresetCreate()
				.setDynamicExecution(preset.getDynamicExecution())
				.setRelatedClassCanonicalName(preset.getRelatedClassCanonicalName())
				.setDescription(preset.getDescription())
				.setName(preset.getName());
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return gridPresetRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return gridPresetRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return gridPresetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return gridPresetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return gridPresetRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return gridPresetRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return gridPresetRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		gridPresetRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		gridPresetRepository.massMerge(toMerge);
	}
}
