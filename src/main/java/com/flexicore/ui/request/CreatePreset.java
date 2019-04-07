package com.flexicore.ui.request;

public class CreatePreset {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public <T extends CreatePreset> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends CreatePreset> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }
}
