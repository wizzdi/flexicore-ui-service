package com.flexicore.ui.service;


import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.FormFieldRepository;
import com.flexicore.ui.model.FormField;
import com.flexicore.ui.request.FormFieldCreate;
import com.flexicore.ui.request.FormFieldFiltering;
import com.flexicore.ui.request.FormFieldUpdate;
import com.wizzdi.flexicore.security.service.BaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;


@Extension
@Component
public class FormFieldService implements Plugin {

	private static final Logger logger=LoggerFactory.getLogger(FormFieldService.class);

	
	@Autowired
	private FormFieldRepository formFieldRepository;

	
	@Autowired
	private UiFieldService uiFieldService;

	public FormField FormFieldUpdate(FormFieldUpdate FormFieldUpdate,
			SecurityContextBase securityContext) {
		if (FormFieldUpdateNoMerge(FormFieldUpdate,
				FormFieldUpdate.getFormField())) {
			formFieldRepository.merge(FormFieldUpdate.getFormField());
		}
		return FormFieldUpdate.getFormField();
	}

	public boolean FormFieldUpdateNoMerge(FormFieldCreate formFieldCreate,
			FormField formField) {
		boolean update = uiFieldService.updateUiFieldNoMerge(formFieldCreate,
				formField);
		if (formFieldCreate.getEditable() != null && formFieldCreate.getEditable() != formField.isEditable()) {
			formField.setEditable(formFieldCreate.getEditable());
			update = true;
		}
		if (formFieldCreate.getCreatable() != null && formFieldCreate.getCreatable() != formField.isCreatable()) {
			formField.setCreatable(formFieldCreate.getCreatable());
			update = true;
		}
		if (formFieldCreate.getAnchorBottom() != null && formFieldCreate.getAnchorBottom() != formField.getAnchorBottom()) {
			formField.setAnchorBottom(formFieldCreate.getAnchorBottom());
			update = true;
		}

		if (formFieldCreate.getAnchorLeft() != null && formFieldCreate.getAnchorLeft() != formField.getAnchorLeft()) {
			formField.setAnchorLeft(formFieldCreate.getAnchorLeft());
			update = true;
		}
		if (formFieldCreate.getAnchorRight() != null && formFieldCreate.getAnchorRight() != formField.getAnchorRight()) {
			formField.setAnchorRight(formFieldCreate.getAnchorRight());
			update = true;
		}

		if (formFieldCreate.getAnchorTop() != null && formFieldCreate.getAnchorTop() != formField.getAnchorTop()) {
			formField.setAnchorTop(formFieldCreate.getAnchorTop());
			update = true;
		}
		return update;
	}


	public List<FormField> listAllFormFields(
			FormFieldFiltering formFieldFiltering,
			SecurityContextBase securityContext) {
		return formFieldRepository.listAllFormFields(formFieldFiltering,
				securityContext);
	}

	public FormField createFormField(FormFieldCreate createFormField,
			SecurityContextBase securityContext) {
		FormField formField = createFormFieldNoMerge(createFormField,
				securityContext);
		formFieldRepository.merge(formField);
		return formField;

	}

	public FormField createFormFieldNoMerge(FormFieldCreate createFormField,
			SecurityContextBase securityContext) {
		FormField formField = new FormField();
		formField.setId(UUID.randomUUID().toString());
		FormFieldUpdateNoMerge(createFormField, formField);
		BaseclassService.createSecurityObjectNoMerge(formField,securityContext);
		return formField;
	}

	public PaginationResponse<FormField> getAllFormFields(
			FormFieldFiltering formFieldFiltering,
			SecurityContextBase securityContext) {
		List<FormField> list = listAllFormFields(formFieldFiltering, securityContext);
		long count = formFieldRepository.countAllFormFields(formFieldFiltering, securityContext);
		return new PaginationResponse<>(list, formFieldFiltering, count);
	}

	public void validate(FormFieldCreate createFormField, SecurityContextBase securityContext) {
		uiFieldService.validate(createFormField, securityContext);
	}

	public void validate(FormFieldFiltering formFieldFiltering, SecurityContextBase securityContext) {
		uiFieldService.validate(formFieldFiltering, securityContext);
	}

	public FormFieldCreate getFormFieldCreate(FormField formField) {
		return new FormFieldCreate().setEditable(formField.isEditable())
				.setCreatable(formField.isCreatable())
				.setAnchorBottom(formField.getAnchorBottom())
				.setAnchorLeft(formField.getAnchorLeft())
				.setAnchorRight(formField.getAnchorRight())
				.setAnchorTop(formField.getAnchorTop())
				.setPreset(formField.getPreset())
				.setCategory(formField.getCategory())
				.setDisplayName(formField.getDisplayName())
				.setVisible(formField.isVisible())
				.setPriority(formField.getPriority())
				.setDescription(formField.getDescription())
				.setName(formField.getName());
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return formFieldRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return formFieldRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return formFieldRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return formFieldRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return formFieldRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return formFieldRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return formFieldRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		formFieldRepository.merge(base);
	}

	public void validateCreate(FormFieldCreate createFormField, SecurityContextBase securityContext) {
		uiFieldService.validateCreate(createFormField,securityContext);
	}
}
