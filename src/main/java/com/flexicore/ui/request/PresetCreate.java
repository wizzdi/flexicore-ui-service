package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.security.request.BasicCreate;

import java.util.Map;

public class PresetCreate extends BasicCreate {

    private Map<String, Object> jsonNode;
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends PresetCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

    @JsonIgnore
    public Map<String, Object> getJsonNode() {
        return this.jsonNode;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return this.jsonNode;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        jsonNode.put(key, value);
    }

    public <T extends PresetCreate> T setJsonNode(Map<String, Object> jsonNode) {
        this.jsonNode = jsonNode;
        return (T) this;
    }
}
