package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.ui.model.CardGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardFiltering extends FilteringInformationHolder {

    private Set<String> cardGroupIds=new HashSet<>();
    @JsonIgnore
    private List<CardGroup> cardGroups;

    public Set<String> getCardGroupIds() {
        return cardGroupIds;
    }

    public <T extends CardFiltering> T setCardGroupIds(Set<String> cardGroupIds) {
        this.cardGroupIds = cardGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<CardGroup> getCardGroups() {
        return cardGroups;
    }

    public <T extends CardFiltering> T setCardGroups(List<CardGroup> cardGroups) {
        this.cardGroups = cardGroups;
        return (T) this;
    }

    @Override
    public boolean supportingDynamic() {
        return true;
    }
}
