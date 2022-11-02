package com.dallmeier.evidencer.model.incident_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncidentType {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
