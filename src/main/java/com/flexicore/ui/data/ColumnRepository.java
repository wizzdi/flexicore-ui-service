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
public class ColumnRepository extends AbstractRepositoryPlugin {


    public void massMerge(List<?> list) {
        for (Object o : list) {
            em.merge(o);
        }
    }


    public List<Column> listAllColumns(ColumnFiltering columnFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Column> q=cb.createQuery(Column.class);
        Root<Column> r=q.from(Column.class);
        List<Predicate> preds=new ArrayList<>();
        addColumnPredicates(preds,cb,r,columnFiltering);
        QueryInformationHolder<Column> queryInformationHolder=new QueryInformationHolder<>(columnFiltering,Column.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addColumnPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<Column> r, ColumnFiltering columnFiltering) {
        if(columnFiltering.getPresets()!=null && !columnFiltering.getPresets().isEmpty()){
            Set<String> ids=columnFiltering.getPresets().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<Column,GridPreset> join=cb.treat(r.join(Column_.preset),GridPreset.class);
            preds.add(join.get(GridPreset_.id).in(ids));
        }
    }

    public long countAllColumns(ColumnFiltering columnFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<Column> r=q.from(Column.class);
        List<Predicate> preds=new ArrayList<>();
        addColumnPredicates(preds,cb,r,columnFiltering);
        QueryInformationHolder<Column> queryInformationHolder=new QueryInformationHolder<>(columnFiltering,Column.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
