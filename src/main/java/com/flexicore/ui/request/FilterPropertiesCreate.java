package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.ui.model.Preset;
import com.wizzdi.flexicore.security.request.BasicCreate;

public class FilterPropertiesCreate extends BasicCreate {

    private String filterPath;
    private Boolean externalize;
    private String baseclassId;
    @JsonIgnore
    private Preset baseclass;

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
    public Preset getBaseclass() {
        return baseclass;
    }

    public <T extends FilterPropertiesCreate> T setBaseclass(Preset baseclass) {
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
