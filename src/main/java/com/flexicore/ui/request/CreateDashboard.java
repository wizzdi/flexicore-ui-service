package com.flexicore.ui.request;

public class CreateDashboard extends CreatePreset{

    private String contextString;



    public String getContextString() {
        return contextString;
    }

    public <T extends CreateDashboard> T setContextString(String contextString) {
        this.contextString = contextString;
        return (T) this;
    }
}
