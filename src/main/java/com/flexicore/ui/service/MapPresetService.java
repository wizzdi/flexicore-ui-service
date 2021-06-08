package com.flexicore.ui.service;


import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.MapPresetRepository;
import com.flexicore.ui.model.MapPreset;
import com.flexicore.ui.request.MapPresetFiltering;
import com.flexicore.ui.request.MapPresetCreate;
import com.flexicore.ui.request.MapPresetUpdate;
import com.wizzdi.flexicore.security.service.BaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;


@Extension
@Component
public class MapPresetService implements Plugin {

	private static final Logger logger=LoggerFactory.getLogger(MapPresetService.class);

	
	@Autowired
	private MapPresetRepository mapPresetRepository;

	
	@Autowired
	private PresetService presetService;

	public MapPreset updateMapPreset(MapPresetUpdate mapPresetUpdate, SecurityContextBase securityContext) {
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


	public PaginationResponse<MapPreset> getAllMapPresets(MapPresetFiltering mapPresetFiltering, SecurityContextBase securityContext) {
		List<MapPreset> list = listAllMapPresets(mapPresetFiltering, securityContext);
		long count = mapPresetRepository.countAllMapPresets(mapPresetFiltering, securityContext);
		return new PaginationResponse<>(list, mapPresetFiltering, count);
	}

	public List<MapPreset> listAllMapPresets(MapPresetFiltering mapPresetFiltering, SecurityContextBase securityContext) {
		return mapPresetRepository.listAllMapPresets(mapPresetFiltering, securityContext);
	}

	public MapPreset createMapPreset(MapPresetCreate mapPresetCreate, SecurityContextBase securityContext) {
		MapPreset mapPreset = createMapPresetNoMerge(mapPresetCreate, securityContext);
		mapPresetRepository.merge(mapPreset);
		return mapPreset;

	}

	private MapPreset createMapPresetNoMerge(MapPresetCreate mapPresetCreate, SecurityContextBase securityContext) {
		MapPreset mapPreset = new MapPreset();
		mapPreset.setId(UUID.randomUUID().toString());
		updateMapPresetNoMerge(mapPresetCreate, mapPreset);
		BaseclassService.createSecurityObjectNoMerge(mapPreset,securityContext);
		return mapPreset;
	}

	public void validate(MapPresetCreate mapPresetCreate, SecurityContextBase securityContext) {
		presetService.validate(mapPresetCreate,securityContext);

	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return mapPresetRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return mapPresetRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return mapPresetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return mapPresetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return mapPresetRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return mapPresetRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return mapPresetRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		mapPresetRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		mapPresetRepository.massMerge(toMerge);
	}
}
