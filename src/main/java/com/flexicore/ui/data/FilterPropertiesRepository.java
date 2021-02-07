package com.flexicore.ui.data;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.model.FilterProperties;
import com.flexicore.ui.model.FilterProperties_;
import com.flexicore.ui.model.GridPreset;
import com.flexicore.ui.model.GridPreset_;
import com.flexicore.ui.request.FilterPropertiesFiltering;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class FilterPropertiesRepository extends AbstractRepositoryPlugin {

	public List<FilterProperties> listAllFilterProperties(FilterPropertiesFiltering filterPropertiesFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FilterProperties> q = cb.createQuery(FilterProperties.class);
		Root<FilterProperties> r = q.from(FilterProperties.class);
		List<Predicate> preds = new ArrayList<>();
		addFilterPropertiesPredicates(preds, cb, r, filterPropertiesFiltering);
		QueryInformationHolder<FilterProperties> queryInformationHolder = new QueryInformationHolder<>(
				filterPropertiesFiltering, FilterProperties.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addFilterPropertiesPredicates(List<Predicate> preds, CriteriaBuilder cb,
			Root<FilterProperties> r, FilterPropertiesFiltering filterPropertiesFiltering) {
		if(filterPropertiesFiltering.getBaseclasses()!=null &&!filterPropertiesFiltering.getBaseclasses().isEmpty()){
			Set<String> ids=filterPropertiesFiltering.getBaseclasses().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<FilterProperties, Baseclass> join=r.join(FilterProperties_.relatedBaseclass);
			preds.add(join.get(GridPreset_.id).in(ids));
		}


	}

	public long countAllFilterProperties(FilterPropertiesFiltering filterPropertiesFiltering,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<FilterProperties> r = q.from(FilterProperties.class);
		List<Predicate> preds = new ArrayList<>();
		addFilterPropertiesPredicates(preds, cb, r, filterPropertiesFiltering);
		QueryInformationHolder<FilterProperties> queryInformationHolder = new QueryInformationHolder<>(
				filterPropertiesFiltering, FilterProperties.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
