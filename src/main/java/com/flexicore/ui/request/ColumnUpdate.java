package com.flexicore.ui.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.ui.model.Column;

public class ColumnUpdate extends ColumnCreate {
    private String id;
    private Column column;





    public ColumnUpdate() {
    }



    public String getId() {
        return id;
    }

    public ColumnUpdate setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public Column getColumn() {
        return column;
    }

    public ColumnUpdate setColumn(Column column) {
        this.column = column;
        return this;
    }

}
