package com.dallmeier.evidencer.model.upload;

import com.dallmeier.evidencer.model.evident.Author;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.inject.Inject;

public class FileMeta {
    @Inject
    public FileMeta() {
    }

    @SerializedName("criticality")
    @Expose
    private Integer criticality;
    @SerializedName("timeStampMillis")
    @Expose
    private long timeStampMillis;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("installationId")
    @Expose
    private Integer installationId;
    @SerializedName("dateObserved")
    @Expose
    private long dateObserved;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("location")
    @Expose
    private Location location;

    public Integer getCriticality() {
        return criticality;
    }

    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    public long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInstallationId() {
        return installationId;
    }

    public void setInstallationId(Integer installationId) {
        this.installationId = installationId;
    }

    public long getDateObserved() {
        return dateObserved;
    }

    public void setDateObserved(long dateObserved) {
        this.dateObserved = dateObserved;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}