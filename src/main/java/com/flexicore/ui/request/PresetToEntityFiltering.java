package com.flexicore.ui.request;

import com.flexicore.request.BaselinkFilter;

public class PresetToEntityFiltering extends BaselinkFilter {
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends PresetToEntityFiltering> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
