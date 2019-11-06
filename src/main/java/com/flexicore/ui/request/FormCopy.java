package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Form;

public class FormCopy extends FormCreate {

    private String id;
    @JsonIgnore
    private Form form;

    public String getId() {
        return id;
    }

    public FormCopy setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public Form getForm() {
        return form;
    }

    public FormCopy setForm(Form form) {
        this.form = form;
        return this;
    }
}
