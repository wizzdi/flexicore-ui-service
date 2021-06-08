package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.ui.model.Preset;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterPropertiesFiltering extends PaginationFilter  {

    private Set<String> baseclassIds =new HashSet<>();
    @JsonIgnore
    private List<Preset> baseclasses;

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends FilterPropertiesFiltering> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Preset> getBaseclasses() {
        return baseclasses;
    }

    public <T extends FilterPropertiesFiltering> T setBaseclasses(List<Preset> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }
}
