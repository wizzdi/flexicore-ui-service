package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.ui.data.DashboardElementRepository;
import com.flexicore.ui.model.Dashboard;
import com.flexicore.ui.model.DashboardElement;
import com.flexicore.ui.request.CreateDashboardElement;
import com.flexicore.ui.request.DashboardElementFiltering;
import com.flexicore.ui.request.UpdateDashboardElement;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class DashboardElementService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private DashboardElementRepository dashboardElementRepository;

	public DashboardElement updateDashboardElement(
			UpdateDashboardElement updateDashboardElement,
			SecurityContext securityContext) {
		if (updateDashboardElementNoMerge(updateDashboardElement,
				updateDashboardElement.getDashboardElement())) {
			dashboardElementRepository.merge(updateDashboardElement
					.getDashboardElement());
		}
		return updateDashboardElement.getDashboardElement();
	}

	public boolean updateDashboardElementNoMerge(
			CreateDashboardElement updateDashboardElement,
			DashboardElement dashboardElement) {
		boolean update = false;

		if (updateDashboardElement.getDescription() != null
				&& (dashboardElement.getDescription() == null || !updateDashboardElement
						.getDescription().equals(
								dashboardElement.getDescription()))) {
			update = true;
			dashboardElement.setDescription(updateDashboardElement
					.getDescription());
		}

		if (updateDashboardElement.getName() != null
				&& !updateDashboardElement.getName().equals(
						dashboardElement.getName())) {
			update = true;
			dashboardElement.setName(updateDashboardElement.getName());
		}

		if (updateDashboardElement.getContextString() != null
				&& !updateDashboardElement.getContextString().equals(
						dashboardElement.getContextString())) {
			update = true;
			dashboardElement.setContextString(updateDashboardElement
					.getContextString());
		}

		if (updateDashboardElement.getDashboard() != null
				&& (dashboardElement.getDashboard() == null || !updateDashboardElement
						.getDashboard().getId()
						.equals(dashboardElement.getDashboard().getId()))) {
			update = true;
			dashboardElement
					.setDashboard(updateDashboardElement.getDashboard());
		}

		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return dashboardElementRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<DashboardElement> getAllDashboardElements(
			DashboardElementFiltering dashboardElementFiltering,
			SecurityContext securityContext) {
		List<DashboardElement> list = listAllDashboardElements(
				dashboardElementFiltering, securityContext);
		long count = dashboardElementRepository.countAllDashboardElements(
				dashboardElementFiltering, securityContext);
		return new PaginationResponse<>(list, dashboardElementFiltering, count);
	}

	public List<DashboardElement> listAllDashboardElements(
			DashboardElementFiltering dashboardElementFiltering,
			SecurityContext securityContext) {
		return dashboardElementRepository.listAllDashboardElements(
				dashboardElementFiltering, securityContext);
	}

	public DashboardElement createDashboardElement(
			CreateDashboardElement createDashboardElement,
			SecurityContext securityContext) {
		DashboardElement dashboardElement = createDashboardElementNoMerge(
				createDashboardElement, securityContext);
		dashboardElementRepository.merge(dashboardElement);
		return dashboardElement;

	}

	private DashboardElement createDashboardElementNoMerge(
			CreateDashboardElement createDashboardElement,
			SecurityContext securityContext) {
		DashboardElement dashboardElement = DashboardElement.s()
				.CreateUnchecked(createDashboardElement.getName(),
						securityContext);
		dashboardElement.Init();
		updateDashboardElementNoMerge(createDashboardElement, dashboardElement);
		return dashboardElement;
	}

	public void validate(CreateDashboardElement createDashboardElement,
			SecurityContext securityContext) {
		String dashboardId = createDashboardElement.getDashboardId();
		Dashboard dashboard = dashboardId != null ? getByIdOrNull(dashboardId,
				Dashboard.class, null, securityContext) : null;
		if (dashboard == null) {
			throw new BadRequestException("No Dashboard element with id "
					+ dashboardId);
		}
		createDashboardElement.setDashboard(dashboard);

	}
}
