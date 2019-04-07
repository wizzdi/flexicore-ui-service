package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Dashboard;
import com.flexicore.ui.request.DashboardFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class DashboardRepository extends AbstractRepositoryPlugin {


    public List<Dashboard> listAllDashboards(DashboardFiltering dashboardFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Dashboard> q=cb.createQuery(Dashboard.class);
        Root<Dashboard> r=q.from(Dashboard.class);
        List<Predicate> preds=new ArrayList<>();
        addDashboardPredicates(preds,cb,r,dashboardFiltering);
        QueryInformationHolder<Dashboard> queryInformationHolder=new QueryInformationHolder<>(dashboardFiltering,Dashboard.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addDashboardPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<Dashboard> r, DashboardFiltering dashboardFiltering) {

    }

    public long countAllDashboards(DashboardFiltering dashboardFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<Dashboard> r=q.from(Dashboard.class);
        List<Predicate> preds=new ArrayList<>();
        addDashboardPredicates(preds,cb,r,dashboardFiltering);
        QueryInformationHolder<Dashboard> queryInformationHolder=new QueryInformationHolder<>(dashboardFiltering,Dashboard.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
