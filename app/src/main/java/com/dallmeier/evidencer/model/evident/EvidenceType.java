package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvidenceType  {
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("mediaDetails")
    @Expose
    private List<MediaDetail> mediaDetails = null;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<MediaDetail> getMediaDetails() {
        return mediaDetails;
    }

    public void setMediaDetails(List<MediaDetail> mediaDetails) {
        this.mediaDetails = mediaDetails;
    }

}
