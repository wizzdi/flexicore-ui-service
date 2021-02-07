package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterPropertiesFiltering extends FilteringInformationHolder {

    private Set<String> baseclassIds =new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends FilterPropertiesFiltering> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends FilterPropertiesFiltering> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }
}
