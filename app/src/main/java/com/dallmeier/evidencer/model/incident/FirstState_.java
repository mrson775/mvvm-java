package com.dallmeier.evidencer.model.incident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirstState_ {
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timeStampCreated")
    @Expose
    private Integer timeStampCreated;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("finalState")
    @Expose
    private Boolean finalState;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("aimsCustomFieldSettings")
    @Expose
    private List<Object> aimsCustomFieldSettings = null;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(Integer timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFinalState() {
        return finalState;
    }

    public void setFinalState(Boolean finalState) {
        this.finalState = finalState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<Object> getAimsCustomFieldSettings() {
        return aimsCustomFieldSettings;
    }

    public void setAimsCustomFieldSettings(List<Object> aimsCustomFieldSettings) {
        this.aimsCustomFieldSettings = aimsCustomFieldSettings;
    }

}
