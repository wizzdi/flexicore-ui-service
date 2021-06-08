package com.flexicore.ui.data;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.flexicore.model.Basic;
import com.flexicore.ui.model.Preset;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.flexicore.model.Baselink_;

import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.model.PresetToEntity;
import com.flexicore.ui.model.PresetToEntity_;
import com.flexicore.ui.request.PresetToEntityFiltering;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Extension
@Component
public class PresetToEntityRepository implements Plugin {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SecuredBasicRepository securedBasicRepository;

    public List<PresetToEntity> listAllPresetToEntities(
            PresetToEntityFiltering presetToEntityFiltering,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToEntity> q = cb.createQuery(PresetToEntity.class);
        Root<PresetToEntity> r = q.from(PresetToEntity.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToEntityPredicates(preds, cb, q, r, presetToEntityFiltering, securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<PresetToEntity> query = em.createQuery(q);
        BasicRepository.addPagination(presetToEntityFiltering, query);
        return query.getResultList();
    }

    public <T extends PresetToEntity> void addPresetToEntityPredicates(List<Predicate> preds,
                                                                       CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r,
                                                                       PresetToEntityFiltering presetToEntityFiltering, SecurityContextBase securityContextBase) {
        securedBasicRepository.addSecuredBasicPredicates(null, cb, q, r, preds, securityContextBase);
        if (presetToEntityFiltering.getEnabled() != null) {
            preds.add(cb.equal(r.get(PresetToEntity_.enabled),
                    presetToEntityFiltering.getEnabled()));
        }
        if (presetToEntityFiltering.getPresets() != null && !presetToEntityFiltering.getPresets().isEmpty()) {
            Set<String> ids = presetToEntityFiltering.getPresets().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, Preset> join = r.join(PresetToEntity_.preset);
            preds.add(join.get(Baseclass_.id).in(ids));
        }
    }

    public long countAllPresetToEntities(
            PresetToEntityFiltering presetToEntityFiltering,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToEntity> r = q.from(PresetToEntity.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToEntityPredicates(preds, cb, q, r, presetToEntityFiltering, securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        BasicRepository.addPagination(presetToEntityFiltering, query);
        return query.getSingleResult();
    }



    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return securedBasicRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return securedBasicRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return securedBasicRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return securedBasicRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return securedBasicRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        securedBasicRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        securedBasicRepository.massMerge(toMerge);
    }
}
