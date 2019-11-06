package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.request.FormFiltering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class FormRepository extends AbstractRepositoryPlugin {


    public List<Form> listAllForms(FormFiltering formFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Form> q=cb.createQuery(Form.class);
        Root<Form> r=q.from(Form.class);
        List<Predicate> preds=new ArrayList<>();
        addFormPredicates(preds,cb,r,formFiltering);
        QueryInformationHolder<Form> queryInformationHolder=new QueryInformationHolder<>(formFiltering,Form.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addFormPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<Form> r, FormFiltering formFiltering) {

    }

    public long countAllForms(FormFiltering formFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<Form> r=q.from(Form.class);
        List<Predicate> preds=new ArrayList<>();
        addFormPredicates(preds,cb,r,formFiltering);
        QueryInformationHolder<Form> queryInformationHolder=new QueryInformationHolder<>(formFiltering,Form.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
