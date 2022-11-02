package com.dallmeier.evidencer.model.incident.create;

import com.dallmeier.evidencer.model.incident.DefaultWorkflow;
import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.incident_response.StateEntity;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import okhttp3.Dispatcher;

public class Incident implements Serializable {
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("timeStampCreated")
    @Expose
    private long timeStampCreated;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("incidentNbo")
    @Expose
    private String incidentNbo;
    @SerializedName("units")
    @Expose
    private List<Object> units = null;
    @SerializedName("boss")
    @Expose
    private Object boss;
    @SerializedName("state")
    @Expose
    private StateEntity state;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zipCode")
    @Expose
    private String zipCode;
    @SerializedName("timeStampFirstObserved")
    @Expose
    private Integer timeStampFirstObserved;
    @SerializedName("customFields")
    @Expose
    private Object customFields;
    @SerializedName("aggregatedEvent")
    @Expose
    private AggregatedEvent aggregatedEvent;
    @SerializedName("dispatcher")
    @Expose
    private Dispatcher dispatcher;
    @SerializedName("scheduleDelete")
    @Expose
    private Object scheduleDelete;
    @SerializedName("incidentType")
    @Expose
    private IncidentTypeEntity incidentTypeEntity;
    @SerializedName("defaultWorkflow")
    @Expose
    private DefaultWorkflow defaultWorkflow;
    @SerializedName("isFirstState")
    @Expose
    private Boolean isFirstState;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("assigneeId")
    @Expose
    private int assigneeId;

    public Incident(String classType, long timeStampCreated, Integer version, String incidentNbo, List<Object> units, Object boss, StateEntity state, String address, String city, String zipCode, Integer timeStampFirstObserved, Object customFields, AggregatedEvent aggregatedEvent, Dispatcher dispatcher, Object scheduleDelete, IncidentTypeEntity incidentTypeEntity, DefaultWorkflow defaultWorkflow, Boolean isFirstState, Location location) {
        this.classType = classType;
        this.timeStampCreated = timeStampCreated;
        this.version = version;
        this.incidentNbo = incidentNbo;
        this.units = units;
        this.boss = boss;
        this.state = state;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.timeStampFirstObserved = timeStampFirstObserved;
        this.customFields = customFields;
        this.aggregatedEvent = aggregatedEvent;
        this.dispatcher = dispatcher;
        this.scheduleDelete = scheduleDelete;
        this.incidentTypeEntity = incidentTypeEntity;
        this.defaultWorkflow = defaultWorkflow;
        this.isFirstState = isFirstState;
        this.location = location;
    }
    public int getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(int assigneeId) {
        this.assigneeId = assigneeId;
    }
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

    public long getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(long timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getIncidentNbo() {
        return incidentNbo + "";
    }

    public void setIncidentNbo(String incidentNbo) {
        this.incidentNbo = incidentNbo;
    }

    public List<Object> getUnits() {
        return units;
    }

    public void setUnits(List<Object> units) {
        this.units = units;
    }

    public Object getBoss() {
        return boss;
    }

    public void setBoss(Object boss) {
        this.boss = boss;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getTimeStampFirstObserved() {
        return timeStampFirstObserved;
    }

    public void setTimeStampFirstObserved(Integer timeStampFirstObserved) {
        this.timeStampFirstObserved = timeStampFirstObserved;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(JsonObject customFields) {
        this.customFields = customFields;
    }

    public AggregatedEvent getAggregatedEvent() {
        return aggregatedEvent;
    }

    public void setAggregatedEvent(AggregatedEvent aggregatedEvent) {
        this.aggregatedEvent = aggregatedEvent;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Object getScheduleDelete() {
        return scheduleDelete;
    }

    public void setScheduleDelete(Object scheduleDelete) {
        this.scheduleDelete = scheduleDelete;
    }

    public IncidentTypeEntity getIncidentTypeEntity() {
        return incidentTypeEntity;
    }

    public void setIncidentTypeEntity(IncidentTypeEntity incidentTypeEntity) {
        this.incidentTypeEntity = incidentTypeEntity;
    }

    public DefaultWorkflow getDefaultWorkflow() {
        return defaultWorkflow;
    }

    public void setDefaultWorkflow(DefaultWorkflow defaultWorkflow) {
        this.defaultWorkflow = defaultWorkflow;
    }

    public Boolean getIsFirstState() {
        return isFirstState;
    }

    public void setIsFirstState(Boolean isFirstState) {
        this.isFirstState = isFirstState;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
