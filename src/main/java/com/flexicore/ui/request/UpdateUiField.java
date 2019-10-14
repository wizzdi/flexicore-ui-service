package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.UiField;

public class UpdateUiField extends UiFieldCreate {
    private String id;
    private UiField uiField;





    public UpdateUiField() {
    }



    public String getId() {
        return id;
    }

    public UpdateUiField setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public UiField getUiField() {
        return uiField;
    }

    public UpdateUiField setUiField(UiField uiField) {
        this.uiField = uiField;
        return this;
    }

}