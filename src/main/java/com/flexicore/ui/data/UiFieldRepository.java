package com.flexicore.ui.data;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.request.UiFieldFiltering;
import com.flexicore.ui.model.*;
import com.flexicore.ui.model.PresetToRole;
import com.flexicore.ui.request.*;
import com.flexicore.ui.request.PresetToRoleFilter;


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
public class UiFieldRepository implements Plugin {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SecuredBasicRepository securedBasicRepository;


    public List<PresetToRole> listAllPresetToRoles(
            PresetToRoleFilter presetToRoleFilter,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToRole> q = cb.createQuery(PresetToRole.class);
        Root<PresetToRole> r = q.from(PresetToRole.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToRolePredicates(preds, cb, q, r, presetToRoleFilter, securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<PresetToRole> query = em.createQuery(q);
        BasicRepository.addPagination(presetToRoleFilter, query);
        return query.getResultList();
    }

    public <T extends PresetToEntity> void addPresetLinkPredicates(
            List<Predicate> preds, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, PresetLinkFilter presetToRoleFilter, SecurityContextBase securityContextBase) {
        if (presetToRoleFilter.getPresets() != null && !presetToRoleFilter.getPresets().isEmpty()) {
            Set<String> ids = presetToRoleFilter.getPresets().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, Preset> join = cb.treat(r.join(PresetToEntity_.preset), Preset.class);
            preds.add(join.get(Preset_.id).in(ids));
        }

    }

    public <T extends PresetToRole> void addPresetToRolePredicates(List<Predicate> preds,
                                                                   CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r,
                                                                   PresetToRoleFilter presetToRoleFilter, SecurityContextBase securityContextBase) {
        addPresetLinkPredicates(preds, cb, q, r, presetToRoleFilter, securityContextBase);
        if (presetToRoleFilter.getRoles() != null && !presetToRoleFilter.getRoles().isEmpty()) {
            Set<String> ids = presetToRoleFilter.getRoles().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, Role> join = cb.treat(r.join(PresetToRole_.entity), Role.class);
            preds.add(join.get(Role_.id).in(ids));
        }

    }

    public long countAllPresetToRoles(PresetToRoleFilter presetToRoleFilter,
                                      SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToRole> r = q.from(PresetToRole.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToRolePredicates(preds, cb, q, r, presetToRoleFilter, securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }

    public List<PresetToTenant> listAllPresetToTenants(
            PresetToTenantFilter presetToTenantFilter,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToTenant> q = cb.createQuery(PresetToTenant.class);
        Root<PresetToTenant> r = q.from(PresetToTenant.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToTenantPredicates(preds, cb, q, r, presetToTenantFilter, securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<PresetToTenant> query = em.createQuery(q);
        BasicRepository.addPagination(presetToTenantFilter, query);
        return query.getResultList();
    }

    public <T extends PresetToTenant> void addPresetToTenantPredicates(List<Predicate> preds,
                                                                       CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r,
                                                                       PresetToTenantFilter presetToTenantFilter, SecurityContextBase securityContextBase) {
        addPresetLinkPredicates(preds, cb, q, r, presetToTenantFilter, securityContextBase);
        if (presetToTenantFilter.getTenants() != null && !presetToTenantFilter.getTenants().isEmpty()) {
            Set<String> ids = presetToTenantFilter.getTenants().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, SecurityTenant> join = cb.treat(r.join(PresetToTenant_.entity), SecurityTenant.class);
            preds.add(join.get(SecurityTenant_.id).in(ids));
        }

    }

    public long countAllPresetToTenants(
            PresetToTenantFilter presetToTenantFilter,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToTenant> r = q.from(PresetToTenant.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToTenantPredicates(preds, cb, q, r, presetToTenantFilter, securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }

    public List<PresetToUser> listAllPresetToUsers(
            PresetToUserFilter presetToUserFilter,
            SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToUser> q = cb.createQuery(PresetToUser.class);
        Root<PresetToUser> r = q.from(PresetToUser.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToUserPredicates(preds, cb,q, r, presetToUserFilter,securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<PresetToUser> query = em.createQuery(q);
        BasicRepository.addPagination(presetToUserFilter, query);
        return query.getResultList();
    }

    public <T extends PresetToUser> void addPresetToUserPredicates(List<Predicate> preds,
                                           CriteriaBuilder cb,CommonAbstractCriteria q, From<?,T> r,
                                           PresetToUserFilter presetToUserFilter,SecurityContextBase securityContextBase) {
        addPresetLinkPredicates(preds, cb,q, r, presetToUserFilter,securityContextBase);
        if (presetToUserFilter.getUsers() != null && !presetToUserFilter.getUsers().isEmpty()) {
            Set<String> ids = presetToUserFilter.getUsers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, SecurityUser> join = cb.treat(r.join(PresetToUser_.entity), SecurityUser.class);
            preds.add(join.get(SecurityUser_.id).in(ids));
        }

    }

    public long countAllPresetToUsers(PresetToUserFilter presetToUserFilter,
                                      SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToUser> r = q.from(PresetToUser.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToUserPredicates(preds, cb,q, r, presetToUserFilter,securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }

    public List<UiField> listAllUiFields(UiFieldFiltering uiFieldFiltering,
                                         SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UiField> q = cb.createQuery(UiField.class);
        Root<UiField> r = q.from(UiField.class);
        List<Predicate> preds = new ArrayList<>();
        addUiFieldPredicates(preds, cb,q, r, uiFieldFiltering,securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<UiField> query = em.createQuery(q);
        BasicRepository.addPagination(uiFieldFiltering, query);
        return query.getResultList();
    }

    public <T extends UiField> void addUiFieldPredicates(List<Predicate> preds,
                                      CriteriaBuilder cb,CommonAbstractCriteria q, From<?,T> r,
                                      UiFieldFiltering uiFieldFiltering,SecurityContextBase securityContextBase) {
        securedBasicRepository.addSecuredBasicPredicates(null,cb,q,r,preds,securityContextBase);
        if (uiFieldFiltering.getPresets() != null && !uiFieldFiltering.getPresets().isEmpty()) {
            Set<String> ids = uiFieldFiltering.getPresets().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, Preset> join = r.join(UiField_.preset);
            preds.add(join.get(Preset_.id).in(ids));
        }
    }

    public long countAllUiFields(UiFieldFiltering uiFieldFiltering,
                                 SecurityContextBase securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<UiField> r = q.from(UiField.class);
        List<Predicate> preds = new ArrayList<>();
        addUiFieldPredicates(preds, cb,q, r, uiFieldFiltering,securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
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
