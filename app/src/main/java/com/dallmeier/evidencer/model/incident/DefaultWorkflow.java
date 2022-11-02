package com.dallmeier.evidencer.model.incident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultWorkflow {
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
    @SerializedName("firstState")
    @Expose
    private FirstState_ firstState;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("defaultWorkflow")
    @Expose
    private Boolean defaultWorkflow;

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

    public FirstState_ getFirstState() {
        return firstState;
    }

    public void setFirstState(FirstState_ firstState) {
        this.firstState = firstState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefaultWorkflow() {
        return defaultWorkflow;
    }

    public void setDefaultWorkflow(Boolean defaultWorkflow) {
        this.defaultWorkflow = defaultWorkflow;
    }

}
