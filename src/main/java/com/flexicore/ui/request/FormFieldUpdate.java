package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.FormField;

public class FormFieldUpdate extends FormFieldCreate {
    private String id;
    private FormField formField;





    public FormFieldUpdate() {
    }



    public String getId() {
        return id;
    }

    public FormFieldUpdate setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public FormField getFormField() {
        return formField;
    }

    public FormFieldUpdate setFormField(FormField formField) {
        this.formField = formField;
        return this;
    }

}
