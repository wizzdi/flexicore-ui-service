package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.request.UiFieldFiltering;
import com.flexicore.ui.model.*;
import com.flexicore.ui.model.PresetToRole;
import com.flexicore.ui.request.*;
import com.flexicore.ui.request.PresetToRoleFilter;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
public class UiFieldRepository extends AbstractRepositoryPlugin {


    public void massMerge(List<?> list) {
        for (Object o : list) {
            em.merge(o);
        }
    }


    private <T extends Baselink> void addPresetLinkPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<T> r, PresetLinkFilter presetToRoleFilter) {
        if (presetToRoleFilter.getPresets() != null && !presetToRoleFilter.getPresets().isEmpty()) {
            Set<String> ids = presetToRoleFilter.getPresets().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<T, Preset> join = cb.treat(r.join(Baselink_.leftside), Preset.class);
            preds.add(join.get(Preset_.id).in(ids));
        }

    }

    public List<PresetToRole> listAllPresetToRoles(PresetToRoleFilter presetToRoleFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToRole> q = cb.createQuery(PresetToRole.class);
        Root<PresetToRole> r = q.from(PresetToRole.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToRolePredicates(preds, cb, r, presetToRoleFilter);
        QueryInformationHolder<PresetToRole> queryInformationHolder = new QueryInformationHolder<>(presetToRoleFilter, PresetToRole.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }


    private void addPresetToRolePredicates(List<Predicate> preds, CriteriaBuilder cb, Root<PresetToRole> r, PresetToRoleFilter presetToRoleFilter) {
        addPresetLinkPredicates(preds, cb, r, presetToRoleFilter);
        if (presetToRoleFilter.getRoles() != null && !presetToRoleFilter.getRoles().isEmpty()) {
            Set<String> ids = presetToRoleFilter.getRoles().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<PresetToRole, Role> join = cb.treat(r.join(Baselink_.rightside), Role.class);
            preds.add(join.get(Role_.id).in(ids));
        }

    }

    public long countAllPresetToRoles(PresetToRoleFilter presetToRoleFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToRole> r = q.from(PresetToRole.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToRolePredicates(preds, cb, r, presetToRoleFilter);
        QueryInformationHolder<PresetToRole> queryInformationHolder = new QueryInformationHolder<>(presetToRoleFilter, PresetToRole.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }


    public List<PresetToTenant> listAllPresetToTenants(PresetToTenantFilter presetToTenantFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToTenant> q = cb.createQuery(PresetToTenant.class);
        Root<PresetToTenant> r = q.from(PresetToTenant.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToTenantPredicates(preds, cb, r, presetToTenantFilter);
        QueryInformationHolder<PresetToTenant> queryInformationHolder = new QueryInformationHolder<>(presetToTenantFilter, PresetToTenant.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }


    private void addPresetToTenantPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<PresetToTenant> r, PresetToTenantFilter presetToTenantFilter) {
        addPresetLinkPredicates(preds, cb, r, presetToTenantFilter);
        if (presetToTenantFilter.getTenants() != null && !presetToTenantFilter.getTenants().isEmpty()) {
            Set<String> ids = presetToTenantFilter.getTenants().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<PresetToTenant, Tenant> join = cb.treat(r.join(Baselink_.rightside), Tenant.class);
            preds.add(join.get(Tenant_.id).in(ids));
        }

    }

    public long countAllPresetToTenants(PresetToTenantFilter presetToTenantFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToTenant> r = q.from(PresetToTenant.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToTenantPredicates(preds, cb, r, presetToTenantFilter);
        QueryInformationHolder<PresetToTenant> queryInformationHolder = new QueryInformationHolder<>(presetToTenantFilter, PresetToTenant.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }


    public List<PresetToUser> listAllPresetToUsers(PresetToUserFilter presetToUserFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PresetToUser> q = cb.createQuery(PresetToUser.class);
        Root<PresetToUser> r = q.from(PresetToUser.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToUserPredicates(preds, cb, r, presetToUserFilter);
        QueryInformationHolder<PresetToUser> queryInformationHolder = new QueryInformationHolder<>(presetToUserFilter, PresetToUser.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }


    private void addPresetToUserPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<PresetToUser> r, PresetToUserFilter presetToUserFilter) {
        addPresetLinkPredicates(preds, cb, r, presetToUserFilter);
        if (presetToUserFilter.getUsers() != null && !presetToUserFilter.getUsers().isEmpty()) {
            Set<String> ids = presetToUserFilter.getUsers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<PresetToUser, User> join = cb.treat(r.join(Baselink_.rightside), User.class);
            preds.add(join.get(User_.id).in(ids));
        }

    }

    public long countAllPresetToUsers(PresetToUserFilter presetToUserFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<PresetToUser> r = q.from(PresetToUser.class);
        List<Predicate> preds = new ArrayList<>();
        addPresetToUserPredicates(preds, cb, r, presetToUserFilter);
        QueryInformationHolder<PresetToUser> queryInformationHolder = new QueryInformationHolder<>(presetToUserFilter, PresetToUser.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }



    public List<UiField> listAllUiFields(UiFieldFiltering uiFieldFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<UiField> q=cb.createQuery(UiField.class);
        Root<UiField> r=q.from(UiField.class);
        List<Predicate> preds=new ArrayList<>();
        addUiFieldPredicates(preds,cb,r,uiFieldFiltering);
        QueryInformationHolder<UiField> queryInformationHolder=new QueryInformationHolder<>(uiFieldFiltering,UiField.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addUiFieldPredicates(List<Predicate> preds, CriteriaBuilder cb, Root<UiField> r, UiFieldFiltering uiFieldFiltering) {
        if(uiFieldFiltering.getPresets()!=null && !uiFieldFiltering.getPresets().isEmpty()){
            Set<String> ids=uiFieldFiltering.getPresets().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<UiField,Preset> join=r.join(UiField_.preset);
            preds.add(join.get(Preset_.id).in(ids));
        }
    }

    public long countAllUiFields(UiFieldFiltering uiFieldFiltering, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<UiField> r=q.from(UiField.class);
        List<Predicate> preds=new ArrayList<>();
        addUiFieldPredicates(preds,cb,r,uiFieldFiltering);
        QueryInformationHolder<UiField> queryInformationHolder=new QueryInformationHolder<>(uiFieldFiltering,UiField.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }


}
