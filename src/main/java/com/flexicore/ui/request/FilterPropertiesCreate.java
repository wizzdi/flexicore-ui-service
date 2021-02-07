package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.request.BaseclassCreate;

public class FilterPropertiesCreate extends BaseclassCreate {

    private String filterPath;
    private Boolean externalize;
    private String baseclassId;
    @JsonIgnore
    private Baseclass baseclass;

    public Boolean getExternalize() {
        return externalize;
    }

    public <T extends FilterPropertiesCreate> T setExternalize(Boolean externalize) {
        this.externalize = externalize;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    public <T extends FilterPropertiesCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends FilterPropertiesCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getFilterPath() {
        return filterPath;
    }

    public <T extends FilterPropertiesCreate> T setFilterPath(String filterPath) {
        this.filterPath = filterPath;
        return (T) this;
    }
}
