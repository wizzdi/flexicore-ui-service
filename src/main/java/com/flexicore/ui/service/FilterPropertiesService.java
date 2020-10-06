package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CreatePermissionGroupLinkRequest;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.events.BaseclassCreated;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.service.PermissionGroupService;
import com.flexicore.service.SecurityService;
import com.flexicore.ui.data.FilterPropertiesRepository;
import com.flexicore.ui.model.FilterProperties;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.FilterPropertiesCreate;
import com.flexicore.ui.request.FilterPropertiesFiltering;
import com.flexicore.ui.request.FilterPropertiesUpdate;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class FilterPropertiesService implements ServicePlugin {

	private static final Logger logger= LoggerFactory.getLogger(FilterPropertiesService.class);


	@PluginInfo(version = 1)
	@Autowired
	private FilterPropertiesRepository filterPropertiesRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	@Autowired
	private SecurityService securityService;
	@Autowired
	private PermissionGroupService permissionGroupService;
	private SecurityContext adminSecurityContext;

	public FilterProperties updateFilterProperties(FilterPropertiesUpdate filterPropertiesUpdate, SecurityContext securityContext) {
		if (FilterPropertiesUpdateNoMerge(filterPropertiesUpdate,
				filterPropertiesUpdate.getFilterProperties())) {
			filterPropertiesRepository.merge(filterPropertiesUpdate.getFilterProperties());
		}
		return filterPropertiesUpdate.getFilterProperties();
	}

	public boolean FilterPropertiesUpdateNoMerge(FilterPropertiesCreate filterPropertiesCreate, FilterProperties filterProperties) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(filterPropertiesCreate, filterProperties);

		if (filterPropertiesCreate.getFilterPath() != null && !filterPropertiesCreate.getFilterPath().equals(filterProperties.getFilterPath())) {
			filterProperties.setFilterPath(filterPropertiesCreate.getFilterPath());
			update = true;
		}

		if (filterPropertiesCreate.getExternalize() != null && filterPropertiesCreate.getExternalize() != filterProperties.isExternalize()) {
			filterProperties.setExternalize(filterPropertiesCreate.getExternalize());
			update = true;
		}

		if (filterPropertiesCreate.getGridPreset() != null &&(filterProperties.getGridPreset()==null|| !filterPropertiesCreate.getGridPreset().getId().equals(filterProperties.getGridPreset().getId()))) {
			filterProperties.setGridPreset(filterPropertiesCreate.getGridPreset());
			update = true;
		}



		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return filterPropertiesRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public List<FilterProperties> listAllFilterProperties(
			FilterPropertiesFiltering filterPropertiesFiltering,
			SecurityContext securityContext) {
		return filterPropertiesRepository.listAllFilterProperties(filterPropertiesFiltering,
				securityContext);
	}

	public FilterProperties createFilterProperties(FilterPropertiesCreate createFilterProperties,
			SecurityContext securityContext) {
		FilterProperties filterProperties = createFilterPropertiesNoMerge(createFilterProperties,
				securityContext);
		filterPropertiesRepository.merge(filterProperties);
		return filterProperties;

	}

	public FilterProperties createFilterPropertiesNoMerge(
			FilterPropertiesCreate createFilterProperties, SecurityContext securityContext) {
		FilterProperties filterProperties = new FilterProperties(createFilterProperties.getName(),
				securityContext);
		FilterPropertiesUpdateNoMerge(createFilterProperties, filterProperties);
		return filterProperties;
	}

	public PaginationResponse<FilterProperties> getAllFilterProperties(
			FilterPropertiesFiltering filterPropertiesFiltering,
			SecurityContext securityContext) {
		List<FilterProperties> list = listAllFilterProperties(filterPropertiesFiltering,
				securityContext);
		long count = filterPropertiesRepository.countAllFilterProperties(
				filterPropertiesFiltering, securityContext);
		return new PaginationResponse<>(list, filterPropertiesFiltering, count);
	}

	public void validate(FilterPropertiesCreate createFilterProperties,
			SecurityContext securityContext) {
		baseclassNewService.validate(createFilterProperties, securityContext);
		String presetId=createFilterProperties.getPresetId();
		GridPreset gridPreset=presetId!=null?getByIdOrNull(presetId,GridPreset.class,null,securityContext):null;
		if(gridPreset==null){
			throw new BadRequestException("No Grid preset with id "+presetId);
		}
		createFilterProperties.setGridPreset(gridPreset);
	}

	public void validate(FilterPropertiesFiltering filterPropertiesFiltering,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(filterPropertiesFiltering, securityContext);
		Set<String> presetIds=filterPropertiesFiltering.getPresetIds();
		Map<String,GridPreset> presetMap=presetIds.isEmpty()?new HashMap<>():filterPropertiesRepository.listByIds(GridPreset.class,presetIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f,(a,b)->a));
		presetIds.removeAll(presetMap.keySet());
		if(!presetIds.isEmpty()){
			throw new BadRequestException("No Grid Presets with ids "+presetIds);
		}
		filterPropertiesFiltering.setGridPresets(new ArrayList<>(presetMap.values()));
	}


	@EventListener
	@Async
	public void handlePresetPermissionGroupCreated(BaseclassCreated<PermissionGroupToBaseclass> baseclassCreated){
		PermissionGroupToBaseclass permissionGroupToBaseclass = baseclassCreated.getBaseclass();
		PermissionGroup permissionGroup=permissionGroupToBaseclass.getLeftside();
		if(permissionGroupToBaseclass.getRightside() instanceof GridPreset){
			SecurityContext securityContext=getAdminSecurityContext();
			GridPreset preset= (GridPreset) permissionGroupToBaseclass.getRightside();
				logger.info("grid preset "+preset.getName() +"("+preset.getId()+") was attached to permission group "+permissionGroup.getName()+"("+permissionGroup.getId()+") , will attach filter properties");
				List<FilterProperties> filterProperties=listAllFilterProperties(new FilterPropertiesFiltering().setGridPresets(Collections.singletonList(preset)),securityContext);
				if(!filterProperties.isEmpty()){
					CreatePermissionGroupLinkRequest createPermissionGroupLinkRequest = new CreatePermissionGroupLinkRequest()
							.setPermissionGroups(Collections.singletonList(permissionGroup))
							.setBaseclasses(new ArrayList<>(filterProperties));
					permissionGroupService.connectPermissionGroupsToBaseclasses(createPermissionGroupLinkRequest,securityContext);
				}




		}

	}

	private SecurityContext getAdminSecurityContext() {
		if(adminSecurityContext==null){
			adminSecurityContext=securityService.getAdminUserSecurityContext();
		}
		return adminSecurityContext;
	}

}
