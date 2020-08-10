package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.service.DynamicInvokersService;
import com.flexicore.ui.data.DashboardExecutionRepository;
import com.flexicore.ui.model.DashboardElement;
import com.flexicore.ui.model.DashboardExecution;
import com.flexicore.ui.request.CreateDashboardExecution;
import com.flexicore.ui.request.DashboardExecutionFiltering;
import com.flexicore.ui.request.UpdateDashboardExecution;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.logging.Logger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class DashboardExecutionService implements ServicePlugin {

	@Autowired
	private Logger logger;

	@PluginInfo(version = 1)
	@Autowired
	private DashboardExecutionRepository dashboardExecutionRepository;

	public DashboardExecution updateDashboardExecution(
			UpdateDashboardExecution updateDashboardExecution,
			SecurityContext securityContext) {
		if (updateDashboardExecutionNoMerge(updateDashboardExecution,
				updateDashboardExecution.getDashboardExecution())) {
			dashboardExecutionRepository.merge(updateDashboardExecution
					.getDashboardExecution());
		}
		return updateDashboardExecution.getDashboardExecution();
	}

	public boolean updateDashboardExecutionNoMerge(
			CreateDashboardExecution updateDashboardElement,
			DashboardExecution dashboardElement) {
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

		if (updateDashboardElement.getDynamicExecution() != null
				&& !updateDashboardElement.getDynamicExecution().equals(
						dashboardElement.getDynamicExecution())) {
			update = true;
			dashboardElement.setDynamicExecution(updateDashboardElement
					.getDynamicExecution());
		}

		if (updateDashboardElement.getDashboardElement() != null
				&& (dashboardElement.getDashboardElement() == null || !updateDashboardElement
						.getDashboardElement().getId()
						.equals(dashboardElement.getDashboardElement().getId()))) {
			update = true;
			dashboardElement.setDashboardElement(updateDashboardElement
					.getDashboardElement());
		}
		return update;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return dashboardExecutionRepository.getByIdOrNull(id, c, batchString,
				securityContext);
	}

	public PaginationResponse<DashboardExecution> getAllDashboardExecutions(
			DashboardExecutionFiltering dashboardExecutionFiltering,
			SecurityContext securityContext) {
		List<DashboardExecution> list = listAllDashboardExecutions(
				dashboardExecutionFiltering, securityContext);
		long count = dashboardExecutionRepository.countAllDashboardExecutions(
				dashboardExecutionFiltering, securityContext);
		return new PaginationResponse<>(list, dashboardExecutionFiltering,
				count);
	}

	public List<DashboardExecution> listAllDashboardExecutions(
			DashboardExecutionFiltering dashboardExecutionFiltering,
			SecurityContext securityContext) {
		return dashboardExecutionRepository.listAllDashboardExecutions(
				dashboardExecutionFiltering, securityContext);
	}

	public DashboardExecution createDashboardExecution(
			CreateDashboardExecution createDashboardExecution,
			SecurityContext securityContext) {
		DashboardExecution dashboardExecution = createDashboardExecutionNoMerge(
				createDashboardExecution, securityContext);
		dashboardExecutionRepository.merge(dashboardExecution);
		return dashboardExecution;

	}

	private DashboardExecution createDashboardExecutionNoMerge(
			CreateDashboardExecution createDashboardExecution,
			SecurityContext securityContext) {
		DashboardExecution dashboardExecution = new DashboardExecution(createDashboardExecution.getName(), securityContext);
		updateDashboardExecutionNoMerge(createDashboardExecution, dashboardExecution);
		return dashboardExecution;
	}

	public void validate(CreateDashboardExecution createDashboardExecution,
			SecurityContext securityContext) {

		String dashboardElementId = createDashboardExecution
				.getDashboardElementId();
		DashboardElement dashboardElement = dashboardElementId != null
				? getByIdOrNull(dashboardElementId, DashboardElement.class,
						null, securityContext) : null;
		if (dashboardElement == null) {
			throw new BadRequestException("No dashboard element with id "
					+ dashboardElementId);
		}
		createDashboardExecution.setDashboardElement(dashboardElement);

		String dynamicExecutionId = createDashboardExecution
				.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId != null
				? getByIdOrNull(dynamicExecutionId, DynamicExecution.class,
						null, securityContext) : null;
		if (dynamicExecution == null) {
			throw new BadRequestException("No DynamicExecution with ids "
					+ dynamicExecutionId);
		}
		createDashboardExecution.setDynamicExecution(dynamicExecution);

	}
}
