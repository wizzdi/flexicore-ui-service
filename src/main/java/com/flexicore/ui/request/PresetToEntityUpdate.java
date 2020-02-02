package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.PresetToEntity;

public class PresetToEntityUpdate extends PresetToEntityCreate{

    private String id;
    @JsonIgnore
    private PresetToEntity presetToEntity;

    public String getId() {
        return id;
    }

    public <T extends PresetToEntityUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PresetToEntity getPresetToEntity() {
        return presetToEntity;
    }

    public <T extends PresetToEntityUpdate> T setPresetToEntity(PresetToEntity presetToEntity) {
        this.presetToEntity = presetToEntity;
        return (T) this;
    }
}
