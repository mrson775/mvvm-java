package com.dallmeier.evidencer.model.incident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncidentCatalog {
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
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

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

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}