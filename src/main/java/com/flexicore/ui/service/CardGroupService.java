package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.ui.data.CardGroupRepository;
import com.flexicore.ui.model.Dashboard;
import com.flexicore.ui.model.CardGroup;
import com.flexicore.ui.request.CardGroupCreate;
import com.flexicore.ui.request.CardGroupFiltering;
import com.flexicore.ui.request.CardGroupUpdate;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class CardGroupService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private CardGroupRepository cardGroupRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public CardGroup updateCardGroup(
			CardGroupUpdate updateCardGroup,
			SecurityContext securityContext) {
		if (updateCardGroupNoMerge(updateCardGroup,
				updateCardGroup.getCardGroup())) {
			cardGroupRepository.merge(updateCardGroup
					.getCardGroup());
		}
		return updateCardGroup.getCardGroup();
	}

	public boolean updateCardGroupNoMerge(CardGroupCreate updateCardGroup, CardGroup cardGroup) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(updateCardGroup,cardGroup);

		if (updateCardGroup.getDashboard() != null && (cardGroup.getDashboard() == null || !updateCardGroup.getDashboard().getId().equals(cardGroup.getDashboard().getId()))) {
			update = true;
			cardGroup.setDashboard(updateCardGroup.getDashboard());
		}

		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return cardGroupRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<CardGroup> getAllCardGroups(
			CardGroupFiltering cardGroupFiltering,
			SecurityContext securityContext) {
		List<CardGroup> list = listAllCardGroups(
				cardGroupFiltering, securityContext);
		long count = cardGroupRepository.countAllCardGroups(
				cardGroupFiltering, securityContext);
		return new PaginationResponse<>(list, cardGroupFiltering, count);
	}

	public List<CardGroup> listAllCardGroups(
			CardGroupFiltering cardGroupFiltering,
			SecurityContext securityContext) {
		return cardGroupRepository.listAllCardGroups(
				cardGroupFiltering, securityContext);
	}

	public CardGroup createCardGroup(
			CardGroupCreate createCardGroup,
			SecurityContext securityContext) {
		CardGroup cardGroup = createCardGroupNoMerge(
				createCardGroup, securityContext);
		cardGroupRepository.merge(cardGroup);
		return cardGroup;

	}

	private CardGroup createCardGroupNoMerge(
			CardGroupCreate createCardGroup,
			SecurityContext securityContext) {
		CardGroup cardGroup = new CardGroup(createCardGroup.getName(), securityContext);
		updateCardGroupNoMerge(createCardGroup, cardGroup);
		return cardGroup;
	}

	public void validate(CardGroupFiltering cardGroupFiltering, SecurityContext securityContext) {
		baseclassNewService.validateFilter(cardGroupFiltering,securityContext);
		Set<String> dashboardIds=cardGroupFiltering.getDashboardPresetIds();
		Map<String,Dashboard> dashboardMap=dashboardIds.isEmpty()?new HashMap<>():cardGroupRepository.listByIds(Dashboard.class,dashboardIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		dashboardIds.removeAll(dashboardMap.keySet());
		if(!dashboardIds.isEmpty()){
			throw new BadRequestException("No dashboard with ids "+dashboardIds);
		}
		cardGroupFiltering.setDashboardPresets(new ArrayList<>(dashboardMap.values()));


	}

	public void validate(CardGroupCreate createCardGroup,
						 SecurityContext securityContext) {
		String dashboardId = createCardGroup.getDashboardId();
		Dashboard dashboard = dashboardId != null ? getByIdOrNull(dashboardId, Dashboard.class, null, securityContext) : null;
		if (dashboard == null) {
			throw new BadRequestException("No Dashboard element with id " + dashboardId);
		}
		createCardGroup.setDashboard(dashboard);

	}
}
