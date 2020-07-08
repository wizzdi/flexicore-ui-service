package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.FormField;
import com.flexicore.ui.model.FormField_;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.GridPreset_;
import com.flexicore.ui.request.FormFieldFiltering;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class FormFieldRepository extends AbstractRepositoryPlugin {

	public void massMerge(List<?> list) {
		for (Object o : list) {
			em.merge(o);
		}
	}

	public List<FormField> listAllFormFields(
			FormFieldFiltering formFieldFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FormField> q = cb.createQuery(FormField.class);
		Root<FormField> r = q.from(FormField.class);
		List<Predicate> preds = new ArrayList<>();
		addFormFieldPredicates(preds, cb, r, formFieldFiltering);
		QueryInformationHolder<FormField> queryInformationHolder = new QueryInformationHolder<>(
				formFieldFiltering, FormField.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addFormFieldPredicates(List<Predicate> preds,
			CriteriaBuilder cb, Root<FormField> r,
			FormFieldFiltering formFieldFiltering) {
		if (formFieldFiltering.getPresets() != null
				&& !formFieldFiltering.getPresets().isEmpty()) {
			Set<String> ids = formFieldFiltering.getPresets().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<FormField, GridPreset> join = cb.treat(
					r.join(FormField_.preset), GridPreset.class);
			preds.add(join.get(GridPreset_.id).in(ids));
		}
	}

	public long countAllFormFields(FormFieldFiltering formFieldFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<FormField> r = q.from(FormField.class);
		List<Predicate> preds = new ArrayList<>();
		addFormFieldPredicates(preds, cb, r, formFieldFiltering);
		QueryInformationHolder<FormField> queryInformationHolder = new QueryInformationHolder<>(
				formFieldFiltering, FormField.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
