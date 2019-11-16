package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.*;
import com.flexicore.ui.request.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
public class TableColumnRepository extends AbstractRepositoryPlugin {


    public void massMerge(List<?> list) {
        for (Object o : list) {
            em.merge(o);
        }
    }


    public List<TableColumn> listAllTableColumns(TableColumnFiltering tableColumnFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<TableColumn> q=cb.createQuery(TableColumn.class);
        Root<TableColumn> r=q.from(TableColumn.class);
        List<Predicate> preds=new ArrayList<>();
        addTableColumnPredicates(preds,cb,r,tableColumnFiltering);
        QueryInformationHolder<TableColumn> queryInformationHolder=new QueryInformationHolder<>(tableColumnFiltering,TableColumn.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addTableColumnPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<TableColumn> r, TableColumnFiltering tableColumnFiltering) {
        if(tableColumnFiltering.getPresets()!=null && !tableColumnFiltering.getPresets().isEmpty()){
            Set<String> ids=tableColumnFiltering.getPresets().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<TableColumn,GridPreset> join=cb.treat(r.join(TableColumn_.preset),GridPreset.class);
            preds.add(join.get(GridPreset_.id).in(ids));
        }
    }

    public long countAllTableColumns(TableColumnFiltering tableColumnFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<TableColumn> r=q.from(TableColumn.class);
        List<Predicate> preds=new ArrayList<>();
        addTableColumnPredicates(preds,cb,r,tableColumnFiltering);
        QueryInformationHolder<TableColumn> queryInformationHolder=new QueryInformationHolder<>(tableColumnFiltering,TableColumn.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
