package com.flexicore.ui.data;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.request.GridPresetFiltering;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Extension
@Component
public class GridPresetRepository implements Plugin {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PresetRepository presetRepository;

    public List<GridPreset> listAllGridPresets(
            GridPresetFiltering gridPresetFiltering,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GridPreset> q = cb.createQuery(GridPreset.class);
        Root<GridPreset> r = q.from(GridPreset.class);
        List<Predicate> preds = new ArrayList<>();
        addGridPresetPredicates(preds, cb,q, r, gridPresetFiltering,securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<GridPreset> query = em.createQuery(q);
        BasicRepository.addPagination(gridPresetFiltering, query);
        return query.getResultList();
    }

    public <T extends GridPreset> void addGridPresetPredicates(List<Predicate> preds,
                                                               CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r,
                                                               GridPresetFiltering gridPresetFiltering,SecurityContextBase securityContextBase) {
        presetRepository.addPresetPredicates(preds, cb,q, r, gridPresetFiltering,securityContextBase);

    }

    public long countAllGridPresets(GridPresetFiltering gridPresetFiltering,
                                    SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<GridPreset> r = q.from(GridPreset.class);
        List<Predicate> preds = new ArrayList<>();
        addGridPresetPredicates(preds, cb,q, r, gridPresetFiltering,securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return presetRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return presetRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return presetRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return presetRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return presetRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return presetRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return presetRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        presetRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        presetRepository.massMerge(toMerge);
    }
}
