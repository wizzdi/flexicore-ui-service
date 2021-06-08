package com.flexicore.ui.service;


import com.flexicore.model.Basic;
import com.flexicore.ui.model.Form_;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.ui.data.FormRepository;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.request.*;

import com.wizzdi.flexicore.security.service.BaseclassService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.metamodel.SingularAttribute;


@Extension
@Component
public class FormService implements Plugin {

	
	@Autowired
	private PresetService presetService;

	
	@Autowired
	private FormRepository formRepository;

	
	@Autowired
	private UiFieldService uiFieldService;
	@Autowired
	private DynamicExecutionService dynamicExecutionService;

	public PaginationResponse<Form> getAllForms(FormFiltering formFiltering,
			SecurityContextBase securityContext) {
		List<Form> list = listAllForms(formFiltering, securityContext);
		long count = formRepository.countAllForms(formFiltering, securityContext);
		return new PaginationResponse<>(list, formFiltering, count);
	}

	public List<Form> listAllForms(FormFiltering formFiltering, SecurityContextBase securityContext) {
		return formRepository.listAllForms(formFiltering, securityContext);
	}

	public boolean updateFormNoMerge(FormCreate createPreset, Form preset) {
		boolean update = presetService.updatePresetNoMerge(createPreset, preset);
		if (createPreset.getDynamicExecution() != null && (preset.getDynamicExecution() == null || !createPreset.getDynamicExecution().getId().equals(preset.getDynamicExecution().getId()))) {
			preset.setDynamicExecution(createPreset.getDynamicExecution());
			update = true;
		}

		return update;
	}

	public Form updateForm(FormUpdate updatePreset,
			SecurityContextBase securityContext) {
		if (updateFormNoMerge(updatePreset, updatePreset.getForm())) {
			formRepository.merge(updatePreset.getForm());
		}
		return updatePreset.getForm();

	}

	public Form createForm(FormCreate createPreset,
			SecurityContextBase securityContext) {
		Form preset = createFormNoMerge(createPreset, securityContext);
		formRepository.merge(preset);
		return preset;

	}

	public Form createFormNoMerge(FormCreate createPreset,
			SecurityContextBase securityContext) {
		Form preset = new Form();
		preset.setId(UUID.randomUUID().toString());
		updateFormNoMerge(createPreset, preset);
		BaseclassService.createSecurityObjectNoMerge(preset,securityContext);
		return preset;
	}


	public void validateCopy(FormCopy formCopy, SecurityContextBase securityContext) {
		validate(formCopy, securityContext);
		String formId = formCopy.getId();
		Form form = formId != null ? getByIdOrNull(formId, Form.class, Form_.security, securityContext) : null;
		if (form == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Grid Preset With id " + formId);
		}
		formCopy.setForm(form);
	}

	public void validate(FormCreate createForm, SecurityContextBase securityContext) {
		String dynamicExecutionId = createForm.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId == null ? null : dynamicExecutionService.getByIdOrNull(dynamicExecutionId, DynamicExecution.class, SecuredBasic_.security, securityContext);
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Dynamic Execution with id " + dynamicExecutionId);
		}
		createForm.setDynamicExecution(dynamicExecution);
	}

	public Form copyForm(FormCopy formCopy, SecurityContextBase securityContext) {
		List<Object> toMerge = new ArrayList<>();
		FormCreate formCreate = getCreateContainer(formCopy.getForm());
		Form form = createFormNoMerge(formCreate, securityContext);
		updateFormNoMerge(formCopy, form);
		toMerge.add(form);
		List<UiField> uiFields = uiFieldService.listAllUiFields(new UiFieldFiltering().setPresets(Collections.singletonList(formCopy.getForm())), securityContext);
		for (UiField uiField : uiFields) {
			UiFieldCreate uiFieldCreate = uiFieldService.getUIFieldCreate(uiField);
			uiFieldCreate.setPreset(form);
			UiField uiFieldNoMerge = uiFieldService.createUiFieldNoMerge(uiFieldCreate, securityContext);
			toMerge.add(uiFieldNoMerge);
		}
		formRepository.massMerge(toMerge);
		return form;

	}

	private FormCreate getCreateContainer(Form preset) {
		return new FormCreate()
				.setDynamicExecution(preset.getDynamicExecution())
				.setDescription(preset.getDescription())
				.setName(preset.getName());
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return formRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return formRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return formRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return formRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return formRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return formRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return formRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		formRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		formRepository.massMerge(toMerge);
	}
}
