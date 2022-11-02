package com.dallmeier.evidencer.model.incident.create;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncidentDto {
    @SerializedName("incident")
    @Expose
    private Incident incident;
    @SerializedName("aggregatedEventDTO")
    @Expose
    private AggregatedEventDTO aggregatedEventDTO;

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public AggregatedEventDTO getAggregatedEventDTO() {
        return aggregatedEventDTO;
    }

    public void setAggregatedEventDTO(AggregatedEventDTO aggregatedEventDTO) {
        this.aggregatedEventDTO = aggregatedEventDTO;
    }

}
