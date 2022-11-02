package com.dallmeier.evidencer.model.incident.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AggregatedEvent {
    @SerializedName("criticality")
    @Expose
    private Integer criticality;
    @SerializedName("timeStampMillis")
    @Expose
    private long timeStampMillis;
    @SerializedName("description")
    @Expose
    private String description;

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

}
