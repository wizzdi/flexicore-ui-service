package com.flexicore.ui.container.request;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class CreatePreset {

    private String name;
    private String description;
    private String relatedClassCanonicalName;
    private List<CreateUiField> uiFields=new ArrayList<>();


    public String getName() {
        return name;
    }

    public CreatePreset setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreatePreset setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<CreateUiField> getUiFields() {
        return uiFields;
    }

    public CreatePreset setUiFields(List<CreateUiField> uiFields) {
        this.uiFields = uiFields;
        return this;
    }

    @ApiModelProperty("used only for create preset, ignored for update preset")
    public String getRelatedClassCanonicalName() {
        return relatedClassCanonicalName;
    }

    public CreatePreset setRelatedClassCanonicalName(String relatedClassCanonicalName) {
        this.relatedClassCanonicalName = relatedClassCanonicalName;
        return this;
    }
}
