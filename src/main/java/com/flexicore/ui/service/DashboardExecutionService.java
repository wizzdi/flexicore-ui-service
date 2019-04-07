package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.service.DynamicInvokersService;
import com.flexicore.ui.data.DashboardExecutionRepository;
import com.flexicore.ui.model.DashboardExecution;
import com.flexicore.ui.request.CreateDashboardExecution;
import com.flexicore.ui.request.DashboardExecutionFiltering;
import com.flexicore.ui.request.UpdateDashboardExecution;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)

public class DashboardExecutionService implements ServicePlugin {

    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private DashboardExecutionRepository dashboardExecutionRepository;

    @Inject
    private DynamicInvokersService dynamicInvokersService;




    public DashboardExecution updateDashboardExecution(UpdateDashboardExecution updateDashboardExecution, SecurityContext securityContext) {
        if (updateDashboardExecutionNoMerge(updateDashboardExecution, updateDashboardExecution.getDashboardExecution())) {
            dashboardExecutionRepository.merge(updateDashboardExecution.getDashboardExecution());
        }
        return updateDashboardExecution.getDashboardExecution();
    }

    public boolean updateDashboardExecutionNoMerge(CreateDashboardExecution updateDashboardExecution, DashboardExecution dashboardExecution) {
        boolean update = dynamicInvokersService.updateDynamicExecutionNoMerge(updateDashboardExecution,dashboardExecution);

        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return dashboardExecutionRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public PaginationResponse<DashboardExecution> getAllDashboardExecutions(DashboardExecutionFiltering dashboardExecutionFiltering, SecurityContext securityContext) {
       List<DashboardExecution> list=listAllDashboardExecutions(dashboardExecutionFiltering,securityContext);
       long count=dashboardExecutionRepository.countAllDashboardExecutions(dashboardExecutionFiltering,securityContext);
       return new PaginationResponse<>(list,dashboardExecutionFiltering,count);
    }

    public List<DashboardExecution> listAllDashboardExecutions(DashboardExecutionFiltering dashboardExecutionFiltering, SecurityContext securityContext) {
       return dashboardExecutionRepository.listAllDashboardExecutions(dashboardExecutionFiltering,securityContext);
    }


    public DashboardExecution createDashboardExecution(CreateDashboardExecution createDashboardExecution, SecurityContext securityContext) {
        DashboardExecution dashboardExecution = createDashboardExecutionNoMerge(createDashboardExecution, securityContext);
        dashboardExecutionRepository.merge(dashboardExecution);
        return dashboardExecution;

    }

    private DashboardExecution createDashboardExecutionNoMerge(CreateDashboardExecution createDashboardExecution, SecurityContext securityContext) {
        DashboardExecution dashboardExecution = DashboardExecution.s().CreateUnchecked(createDashboardExecution.getName(), securityContext);
        dashboardExecution.Init();
        updateDashboardExecutionNoMerge(createDashboardExecution, dashboardExecution);
        return dashboardExecution;
    }

    public void validate(CreateDashboardExecution createDashboardExecution, SecurityContext securityContext) {

    }
}
