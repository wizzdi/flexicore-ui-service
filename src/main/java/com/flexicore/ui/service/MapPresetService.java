package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.data.MapPresetRepository;
import com.flexicore.ui.model.MapPreset;
import com.flexicore.ui.request.MapPresetFiltering;
import com.flexicore.ui.request.MapPresetCreate;
import com.flexicore.ui.request.MapPresetUpdate;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)
@Extension
@Component
public class MapPresetService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private MapPresetRepository mapPresetRepository;

	@PluginInfo(version = 1)
	@Autowired
	private PresetService presetService;

	public MapPreset updateMapPreset(MapPresetUpdate mapPresetUpdate, SecurityContext securityContext) {
		if (updateMapPresetNoMerge(mapPresetUpdate, mapPresetUpdate.getMapPreset())) {
			mapPresetRepository.merge(mapPresetUpdate.getMapPreset());
		}
		return mapPresetUpdate.getMapPreset();
	}

	public boolean updateMapPresetNoMerge(MapPresetCreate mapPresetCreate, MapPreset mapPreset) {
		boolean update = presetService.updatePresetNoMerge(mapPresetCreate, mapPreset);

		if (mapPresetCreate.getLatCenter() != null && !mapPresetCreate.getLatCenter().equals(mapPreset.getLatCenter())) {
			mapPreset.setLatCenter(mapPresetCreate.getLatCenter());
			update = true;
		}
		if (mapPresetCreate.getLonCenter() != null && !mapPresetCreate.getLonCenter().equals(mapPreset.getLonCenter())) {
			mapPreset.setLonCenter(mapPresetCreate.getLonCenter());
			update = true;
		}
		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return mapPresetRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<MapPreset> getAllMapPresets(MapPresetFiltering mapPresetFiltering, SecurityContext securityContext) {
		List<MapPreset> list = listAllMapPresets(
				mapPresetFiltering, securityContext);
		long count = mapPresetRepository.countAllMapPresets(mapPresetFiltering, securityContext);
		return new PaginationResponse<>(list, mapPresetFiltering,
				count);
	}

	public List<MapPreset> listAllMapPresets(MapPresetFiltering mapPresetFiltering, SecurityContext securityContext) {
		return mapPresetRepository.listAllMapPresets(
				mapPresetFiltering, securityContext);
	}

	public MapPreset createMapPreset(MapPresetCreate mapPresetCreate, SecurityContext securityContext) {
		MapPreset mapPreset = createMapPresetNoMerge(mapPresetCreate, securityContext);
		mapPresetRepository.merge(mapPreset);
		return mapPreset;

	}

	private MapPreset createMapPresetNoMerge(MapPresetCreate mapPresetCreate, SecurityContext securityContext) {
		MapPreset mapPreset = new MapPreset(mapPresetCreate.getName(), securityContext);
		updateMapPresetNoMerge(mapPresetCreate, mapPreset);
		return mapPreset;
	}

	public void validate(MapPresetCreate mapPresetCreate,
			SecurityContext securityContext) {

	}
}
