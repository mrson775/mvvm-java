package com.dallmeier.evidencer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Criteria {
    public Criteria(String property, String operation, List<String> values) {
        this.property = property;
        this.operation = operation;
        this.values = values;
    }

    @SerializedName("property")
    @Expose
    private String property;
    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("values")
    @Expose
    private List<String> values = null;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
