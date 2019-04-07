package com.flexicore.ui.container.request;


import com.flexicore.ui.request.CreatePreset;

import java.util.ArrayList;
import java.util.List;

public class CreateGridPreset extends CreatePreset {

    private String relatedClassCanonicalName;
    private List<CreateUiField> uiFields = new ArrayList<>();


    public List<CreateUiField> getUiFields() {
        return uiFields;
    }

    public CreateGridPreset setUiFields(List<CreateUiField> uiFields) {
        this.uiFields = uiFields;
        return this;
    }

    public String getRelatedClassCanonicalName() {
        return relatedClassCanonicalName;
    }

    public CreateGridPreset setRelatedClassCanonicalName(String relatedClassCanonicalName) {
        this.relatedClassCanonicalName = relatedClassCanonicalName;
        return this;
    }
}
