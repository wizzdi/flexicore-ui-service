package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baselink_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.container.request.UiFieldsForClazzFiltering;
import com.flexicore.ui.model.UiFieldToClazz;
import com.flexicore.ui.model.UiFieldToClazz_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class UiFieldRepository extends AbstractRepositoryPlugin {
    public List<UiFieldToClazz> listAllUiFieldsForClazz(UiFieldsForClazzFiltering uiFieldFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<UiFieldToClazz> q=cb.createQuery(UiFieldToClazz.class);
        Root<UiFieldToClazz> r=q.from(UiFieldToClazz.class);
        List<Predicate> preds=new ArrayList<>();
        preds.add(cb.equal(r.get(Baselink_.rightside),uiFieldFiltering.getClazz()));
        QueryInformationHolder<UiFieldToClazz> queryInformationHolder=new QueryInformationHolder<>(uiFieldFiltering,UiFieldToClazz.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
