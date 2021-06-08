package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.request.PaginationFilter;


import java.util.List;
import java.util.Set;

public class PresetFiltering extends PaginationFilter {

    private Set<String> externalIds;
    @JsonIgnore
    private List<Baseclass> relatedBaseclass;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PresetFiltering> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getRelatedBaseclass() {
        return relatedBaseclass;
    }

    public <T extends PresetFiltering> T setRelatedBaseclass(List<Baseclass> relatedBaseclass) {
        this.relatedBaseclass = relatedBaseclass;
        return (T) this;
    }
}
