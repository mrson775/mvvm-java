package com.dallmeier.evidencer.model.incident.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AggregatedEventDTO {
    @SerializedName("aggregatedEvent")
    @Expose
    private AggregatedEvent aggregatedEvent;
    @SerializedName("eventIds")
    @Expose
    private List<Object> eventIds = new ArrayList<>();

    public AggregatedEvent getAggregatedEvent() {
        return aggregatedEvent;
    }

    public void setAggregatedEvent(AggregatedEvent aggregatedEvent) {
        this.aggregatedEvent = aggregatedEvent;
    }

    public List<Object> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Object> eventIds) {
        this.eventIds = eventIds;
    }

}