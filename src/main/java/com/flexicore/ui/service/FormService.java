package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.data.FormRepository;
import com.flexicore.ui.model.Form;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.request.*;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class FormService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private PresetService presetService;

	@PluginInfo(version = 1)
	@Autowired
	private FormRepository formRepository;

	@PluginInfo(version = 1)
	@Autowired
	private UiFieldService uiFieldService;

	public PaginationResponse<Form> getAllForms(FormFiltering formFiltering,
			SecurityContext securityContext) {
		List<Form> list = listAllForms(formFiltering, securityContext);
		long count = formRepository.countAllForms(formFiltering,
				securityContext);
		return new PaginationResponse<>(list, formFiltering, count);
	}

	public List<Form> listAllForms(FormFiltering formFiltering,
			SecurityContext securityContext) {
		return formRepository.listAllForms(formFiltering, securityContext);
	}

	public boolean updateFormNoMerge(FormCreate createPreset, Form preset) {
		boolean update = presetService
				.updatePresetNoMerge(createPreset, preset);

		if (createPreset.getDynamicExecution() != null
				&& (preset.getDynamicExecution() == null || !createPreset
						.getDynamicExecution().getId()
						.equals(preset.getDynamicExecution().getId()))) {
			preset.setDynamicExecution(createPreset.getDynamicExecution());
			update = true;
		}

		return update;
	}

	public Form updateForm(FormUpdate updatePreset,
			SecurityContext securityContext) {
		if (updateFormNoMerge(updatePreset, updatePreset.getForm())) {
			formRepository.merge(updatePreset.getForm());
		}
		return updatePreset.getForm();

	}

	public Form createForm(FormCreate createPreset,
			SecurityContext securityContext) {
		Form preset = createFormNoMerge(createPreset, securityContext);
		formRepository.merge(preset);
		return preset;

	}

	public Form createFormNoMerge(FormCreate createPreset,
			SecurityContext securityContext) {
		Form preset = Form.s().CreateUnchecked(createPreset.getName(),
				securityContext);
		preset.Init();
		updateFormNoMerge(createPreset, preset);
		return preset;
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return formRepository
				.getByIdOrNull(id, c, batchString, securityContext);
	}

	public void validateCopy(FormCopy formCopy, SecurityContext securityContext) {
		validate(formCopy, securityContext);
		String formId = formCopy.getId();
		Form form = formId != null ? getByIdOrNull(formId, Form.class, null,
				securityContext) : null;
		if (form == null) {
			throw new BadRequestException("No Grid Preset With id " + formId);
		}
		formCopy.setForm(form);
	}

	public void validate(FormCreate createForm, SecurityContext securityContext) {
		String dynamicExecutionId = createForm.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId == null
				? null
				: getByIdOrNull(dynamicExecutionId, DynamicExecution.class,
						null, securityContext);
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new BadRequestException("No Dynamic Execution with id "
					+ dynamicExecutionId);
		}
		createForm.setDynamicExecution(dynamicExecution);
	}

	public Form copyForm(FormCopy formCopy, SecurityContext securityContext) {
		List<Object> toMerge = new ArrayList<>();
		FormCreate formCreate = getCreateContainer(formCopy.getForm());
		Form form = createFormNoMerge(formCreate, securityContext);
		updateFormNoMerge(formCopy, form);
		toMerge.add(form);
		List<UiField> uiFields = uiFieldService.listAllUiFields(
				new UiFieldFiltering().setPresets(Collections
						.singletonList(formCopy.getForm())), securityContext);
		for (UiField uiField : uiFields) {
			UiFieldCreate uiFieldCreate = uiFieldService
					.getUIFieldCreate(uiField);
			uiFieldCreate.setPreset(form);
			UiField uiFieldNoMerge = uiFieldService.createUiFieldNoMerge(
					uiFieldCreate, securityContext);
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
}
