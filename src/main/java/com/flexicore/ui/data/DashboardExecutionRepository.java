package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.DashboardExecution;
import com.flexicore.ui.request.DashboardExecutionFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class DashboardExecutionRepository extends AbstractRepositoryPlugin {


    public List<DashboardExecution> listAllDashboardExecutions(DashboardExecutionFiltering dashboardExecutionFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<DashboardExecution> q=cb.createQuery(DashboardExecution.class);
        Root<DashboardExecution> r=q.from(DashboardExecution.class);
        List<Predicate> preds=new ArrayList<>();
        addDashboardExecutionPredicates(preds,cb,r,dashboardExecutionFiltering);
        QueryInformationHolder<DashboardExecution> queryInformationHolder=new QueryInformationHolder<>(dashboardExecutionFiltering,DashboardExecution.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addDashboardExecutionPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<DashboardExecution> r, DashboardExecutionFiltering dashboardExecutionFiltering) {

    }

    public long countAllDashboardExecutions(DashboardExecutionFiltering dashboardExecutionFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<DashboardExecution> r=q.from(DashboardExecution.class);
        List<Predicate> preds=new ArrayList<>();
        addDashboardExecutionPredicates(preds,cb,r,dashboardExecutionFiltering);
        QueryInformationHolder<DashboardExecution> queryInformationHolder=new QueryInformationHolder<>(dashboardExecutionFiltering,DashboardExecution.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
