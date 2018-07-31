package com.flexicore.ui.container.response;

import com.flexicore.data.jsoncontainers.BasicContainer;
import com.flexicore.ui.model.UiFieldToClazz;

public class UiFieldContainer extends BasicContainer {
    private int priority;
    private boolean visible;
    private String context;
    private String linkId;


    public UiFieldContainer(UiFieldToClazz base) {
        super(base.getLeftside());
        this.priority=base.getPriority();
        this.visible=base.isVisible();
        this.context=base.getContext();
        this.linkId=base.getId();
    }


    public int getPriority() {
        return priority;
    }

    public UiFieldContainer setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public UiFieldContainer setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public String getContext() {
        return context;
    }

    public UiFieldContainer setContext(String context) {
        this.context = context;
        return this;
    }

    public String getLinkId() {
        return linkId;
    }

    public UiFieldContainer setLinkId(String linkId) {
        this.linkId = linkId;
        return this;
    }
}
