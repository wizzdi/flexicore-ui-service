package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaselinkService;
import com.flexicore.ui.data.DashboardElementRepository;
import com.flexicore.ui.model.DashboardElement;
import com.flexicore.ui.request.CreateDashboardElement;
import com.flexicore.ui.request.DashboardElementFiltering;
import com.flexicore.ui.request.UpdateDashboardElement;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)

public class DashboardElementService implements ServicePlugin {

    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private DashboardElementRepository dashboardElementRepository;



    public DashboardElement updateDashboardElement(UpdateDashboardElement updateDashboardElement, SecurityContext securityContext) {
        if (updateDashboardElementNoMerge(updateDashboardElement, updateDashboardElement.getDashboardElement())) {
            dashboardElementRepository.merge(updateDashboardElement.getDashboardElement());
        }
        return updateDashboardElement.getDashboardElement();
    }

    public boolean updateDashboardElementNoMerge(CreateDashboardElement updateDashboardElement, DashboardElement dashboardElement) {
        boolean update = false;


        if (updateDashboardElement.getDescription() != null && (dashboardElement.getDescription() == null || !updateDashboardElement.getDescription().equals(dashboardElement.getDescription()))) {
            update = true;
            dashboardElement.setDescription(updateDashboardElement.getDescription());
        }

        if (updateDashboardElement.getName() != null && !updateDashboardElement.getName().equals(dashboardElement.getName())) {
            update = true;
            dashboardElement.setName(updateDashboardElement.getName());
        }

        if (updateDashboardElement.getContextString() != null && !updateDashboardElement.getContextString().equals(dashboardElement.getContextString())) {
            update = true;
            dashboardElement.setContextString(updateDashboardElement.getContextString());
        }
        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return dashboardElementRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public PaginationResponse<DashboardElement> getAllDashboardElements(DashboardElementFiltering dashboardElementFiltering, SecurityContext securityContext) {
       List<DashboardElement> list=listAllDashboardElements(dashboardElementFiltering,securityContext);
       long count=dashboardElementRepository.countAllDashboardElements(dashboardElementFiltering,securityContext);
       return new PaginationResponse<>(list,dashboardElementFiltering,count);
    }

    public List<DashboardElement> listAllDashboardElements(DashboardElementFiltering dashboardElementFiltering, SecurityContext securityContext) {
       return dashboardElementRepository.listAllDashboardElements(dashboardElementFiltering,securityContext);
    }


    public DashboardElement createDashboardElement(CreateDashboardElement createDashboardElement, SecurityContext securityContext) {
        DashboardElement dashboardElement = createDashboardElementNoMerge(createDashboardElement, securityContext);
        dashboardElementRepository.merge(dashboardElement);
        return dashboardElement;

    }

    private DashboardElement createDashboardElementNoMerge(CreateDashboardElement createDashboardElement, SecurityContext securityContext) {
        DashboardElement dashboardElement = DashboardElement.s().CreateUnchecked(createDashboardElement.getName(), securityContext);
        dashboardElement.Init();
        updateDashboardElementNoMerge(createDashboardElement, dashboardElement);
        return dashboardElement;
    }

    public void validate(CreateDashboardElement createDashboardElement, SecurityContext securityContext) {

    }
}
