package com.flexicore.ui.request;

import com.flexicore.model.FilteringInformationHolder;

import java.util.Set;

public class PresetFiltering extends FilteringInformationHolder {

    private Set<String> externalIds;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PresetFiltering> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }
}
