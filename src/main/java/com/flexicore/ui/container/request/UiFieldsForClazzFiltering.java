package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.data.jsoncontainers.FilteringInformationHolder;
import com.flexicore.model.Clazz;

public class UiFieldsForClazzFiltering extends FilteringInformationHolder {

    private String clazzId;
    @JsonIgnore
    private Clazz clazz;


    public String getClazzId() {
        return clazzId;
    }

    public UiFieldsForClazzFiltering setClazzId(String clazzId) {
        this.clazzId = clazzId;
        return this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return clazz;
    }

    public UiFieldsForClazzFiltering setClazz(Clazz clazz) {
        this.clazz = clazz;
        return this;
    }
}
