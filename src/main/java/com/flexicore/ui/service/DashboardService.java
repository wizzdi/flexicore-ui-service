package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.ui.data.DashboardRepository;
import com.flexicore.ui.model.Dashboard;
import com.flexicore.ui.request.CreateDashboard;
import com.flexicore.ui.request.DashboardFiltering;
import com.flexicore.ui.request.UpdateDashboard;

import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class DashboardService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private DashboardRepository dashboardRepository;

	@PluginInfo(version = 1)
	@Autowired
	private PresetService presetService;

	public Dashboard updateDashboard(UpdateDashboard updateDashboard,
			SecurityContext securityContext) {
		if (updateDashboardNoMerge(updateDashboard,
				updateDashboard.getDashboard())) {
			dashboardRepository.merge(updateDashboard.getDashboard());
		}
		return updateDashboard.getDashboard();
	}

	public boolean updateDashboardNoMerge(CreateDashboard createDashboard,
			Dashboard dashboard) {
		boolean update = presetService.updatePresetNoMerge(createDashboard,
				dashboard);
		if (createDashboard.getContextString() != null
				&& !createDashboard.getContextString().equals(
						dashboard.getContextString())) {
			update = true;
			dashboard.setContextString(createDashboard.getContextString());
		}
		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return dashboardRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<Dashboard> getAllDashboards(
			DashboardFiltering dashboardFiltering,
			SecurityContext securityContext) {
		List<Dashboard> list = listAllDashboards(dashboardFiltering,
				securityContext);
		long count = dashboardRepository.countAllDashboards(dashboardFiltering,
				securityContext);
		return new PaginationResponse<>(list, dashboardFiltering, count);
	}

	public List<Dashboard> listAllDashboards(
			DashboardFiltering dashboardFiltering,
			SecurityContext securityContext) {
		return dashboardRepository.listAllDashboards(dashboardFiltering,
				securityContext);
	}

	public Dashboard createDashboard(CreateDashboard createDashboard,
			SecurityContext securityContext) {
		Dashboard dashboard = createDashboardNoMerge(createDashboard,
				securityContext);
		dashboardRepository.merge(dashboard);
		return dashboard;

	}

	private Dashboard createDashboardNoMerge(CreateDashboard createDashboard,
			SecurityContext securityContext) {
		Dashboard dashboard = Dashboard.s().CreateUnchecked(
				createDashboard.getName(), securityContext);
		dashboard.Init();
		updateDashboardNoMerge(createDashboard, dashboard);
		return dashboard;
	}

	public void validate(CreateDashboard createDashboard,
			SecurityContext securityContext) {

	}
}
