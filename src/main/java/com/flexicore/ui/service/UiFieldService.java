package com.flexicore.ui.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.security.SecurityContext;
import com.flexicore.ui.container.request.*;
import com.flexicore.ui.container.response.UiFieldContainer;
import com.flexicore.ui.data.UiFieldRepository;
import com.flexicore.ui.model.UiField;
import com.flexicore.ui.model.UiFieldToClazz;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@PluginInfo(version = 1)

public class UiFieldService implements ServicePlugin {

    private AtomicBoolean init = new AtomicBoolean(false);
    @Inject
    private Logger logger;

    @Inject
    @PluginInfo(version = 1)
    private UiFieldRepository uiFieldRepository;


    public UiFieldToClazz linkUiFieldToClazz(LinkUiFieldRequest linkUiFieldRequest, SecurityContext securityContext) {
        UiFieldToClazz uiFieldToClazz=UiFieldToClazz.s().CreateUnchecked("link",securityContext.getUser());
        uiFieldToClazz.Init(linkUiFieldRequest.getUiField(),linkUiFieldRequest.getClazz());
        updateUiFieldToClazzLinkNoMerge(new UpdateUiFieldLink(uiFieldToClazz,linkUiFieldRequest));
        uiFieldRepository.merge(uiFieldToClazz);
        return uiFieldToClazz;
    }

    public UiFieldToClazz updateUiFieldToClazzLink(UpdateUiFieldLink updateUiFieldLink, SecurityContext securityContext) {
        if(updateUiFieldToClazzLinkNoMerge(updateUiFieldLink)){
            uiFieldRepository.merge(updateUiFieldLink.getLink());
        }
        return updateUiFieldLink.getLink();
    }

    public boolean updateUiFieldToClazzLinkNoMerge(UpdateUiFieldLink updateUiFieldLink){
        boolean update=false;
        UiFieldToClazz uiFieldToClazz=updateUiFieldLink.getLink();
        if(updateUiFieldLink.getVisibility()!=null && updateUiFieldLink.getVisibility()!=uiFieldToClazz.isVisible()){
            update=true;
            uiFieldToClazz.setVisible(updateUiFieldLink.getVisibility());
        }

        if(updateUiFieldLink.getPriority()!=null && updateUiFieldLink.getPriority()!=uiFieldToClazz.getPriority()){
            update=true;
            uiFieldToClazz.setPriority(updateUiFieldLink.getPriority());
        }

        if(updateUiFieldLink.getContext()!=null && !updateUiFieldLink.getContext().equals(uiFieldToClazz.getContext())){
            update=true;
            uiFieldToClazz.setContext(updateUiFieldLink.getContext());
        }

        if(updateUiFieldLink.getCategoryName()!=null && !updateUiFieldLink.getCategoryName().equals(uiFieldToClazz.getCategoryName())){
            update=true;
            uiFieldToClazz.setCategoryName(updateUiFieldLink.getCategoryName());
        }
        return update;
    }


    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return uiFieldRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public List<UiField> listAllUiFields(UiFieldFiltering uiFieldFiltering, SecurityContext securityContext) {
        QueryInformationHolder<UiField> queryInformationHolder=new QueryInformationHolder<>(uiFieldFiltering,UiField.class,securityContext);
        return uiFieldRepository.getAllFiltered(queryInformationHolder);
    }


    public UiField createUiField(CreateUiField createUiField, SecurityContext securityContext) {
        UiField uiField=UiField.s().CreateUnchecked(createUiField.getName(),securityContext.getUser());
        uiField.Init();
        uiField.setDescription(createUiField.getDescription());
        uiFieldRepository.merge(uiField);
        return uiField;

    }

    public List<UiFieldToClazz> listAllUiFieldsForClazz(UiFieldsForClazzFiltering uiFieldFiltering, SecurityContext securityContext) {
        return uiFieldRepository.listAllUiFieldsForClazz(uiFieldFiltering,securityContext);
    }
}
