package com.flexicore.ui.container.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.UiFieldToClazz;

public class UpdateUiFieldLink {
    private String id;
    private UiFieldToClazz link;
    private Integer priority;
    private Boolean visibility;
    private String context;

    public UpdateUiFieldLink() {
    }


    public UpdateUiFieldLink(UiFieldToClazz link, LinkUiFieldRequest linkUiFieldRequest) {
        this.link = link;
        this.priority = linkUiFieldRequest.getPriority();
        this.visibility = linkUiFieldRequest.isVisible();
        this.context = linkUiFieldRequest.getContext();
    }

    public String getId() {
        return id;
    }

    public UpdateUiFieldLink setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public UiFieldToClazz getLink() {
        return link;
    }

    public UpdateUiFieldLink setLink(UiFieldToClazz link) {
        this.link = link;
        return this;
    }

    public Integer getPriority() {
        return priority;
    }

    public UpdateUiFieldLink setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public UpdateUiFieldLink setVisibility(Boolean visibility) {
        this.visibility = visibility;
        return this;
    }

    public String getContext() {
        return context;
    }

    public UpdateUiFieldLink setContext(String context) {
        this.context = context;
        return this;
    }
}
