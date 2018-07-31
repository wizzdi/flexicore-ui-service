package com.flexicore.ui.container.request;

public class CreateUiField {
   private String name;
   private String description;

    public CreateUiField() {
    }


    public String getName() {
        return name;
    }

    public CreateUiField setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateUiField setDescription(String description) {
        this.description = description;
        return this;
    }
}
