package com.dallmeier.evidencer.model.incident_response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "incident")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncidentEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long idPrimary;
    @ColumnInfo(name = "isRead", defaultValue = "false")
    private boolean isRead;
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "stateId")
    private long stateId;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @SerializedName("distanceToLocation")
    @Expose
    private Double distanceToLocation;
    @SerializedName("travelTimeSeconds")
    @Expose
    private Integer travelTimeSeconds;
    @SerializedName("route")
    @Ignore
    @Expose
    private Route route;
    @ColumnInfo(name = "incidentNbo")
    private String incidentNbo;
    @ColumnInfo(name = "type")
    private String type;
    @Ignore
    @SerializedName("aggregatedEvent")
    @Expose
    private AggregatedEvent aggregatedEvent;
    @SerializedName("customFields")
    @Expose
    @Ignore
    private Object customFields;
    @SerializedName("location")
    @Expose
    @Ignore
    private Location location;
    @SerializedName("timeStampCreated")
    @Expose
    private Long timeStampCreated;
    @SerializedName("state")
    @Ignore
    @Expose
    private StateEntity state;
    @ColumnInfo(name = "incidentTypeId")
    private long incidentTypeId;
    @ColumnInfo(name = "AggregatedEventId")
    private long AggregatedEventId;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "assigneeId")
    private long assigneeId;
    @ColumnInfo(name = "numberAllEvidences")
    private int numberAllEvidences;
    @ColumnInfo(name = "thumbnailURL")
    private String thumbnailURL;
    @ColumnInfo(name = "numberComment", defaultValue = "0")
    private int numberComment;

    @SerializedName("AggregatedEvent")
    public String AggregatedEvent() {
        Gson gson = new Gson();
        return getAggregatedEvent() == null ? null : gson.toJson(getAggregatedEvent());
    }

    public int getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(int numberComment) {
        this.numberComment = numberComment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public int getNumberAllEvidences() {
        return numberAllEvidences;
    }

    public void setNumberAllEvidences(int numberAllEvidences) {
        this.numberAllEvidences = numberAllEvidences;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public long getIncidentTypeId() {
        return incidentTypeId;
    }

    public void setIncidentTypeId(long incidentTypeId) {
        this.incidentTypeId = incidentTypeId;
    }

    public Double getDistanceToLocation() {
        if (distanceToLocation == null) {
            return 0.0;
        }
        return distanceToLocation;
    }

    public void setDistanceToLocation(Double distanceToLocation) {
        this.distanceToLocation = distanceToLocation;
    }

    public Integer getTravelTimeSeconds() {
        return travelTimeSeconds;
    }

    public void setTravelTimeSeconds(Integer travelTimeSeconds) {
        this.travelTimeSeconds = travelTimeSeconds;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getIncidentNbo() {
        return incidentNbo != null ? incidentNbo : "";
    }

    public void setIncidentNbo(String incidentNbo) {
        this.incidentNbo = incidentNbo;
    }

    public AggregatedEvent getAggregatedEvent() {
        return aggregatedEvent;
    }

    public long getLastAggregatedId() {
        try {
            return getAggregatedEvent().getId();
        } catch (Exception e) {
            return AggregatedEventId;
        }
    }

    public void setAggregatedEvent(AggregatedEvent aggregatedEvent) {
        this.aggregatedEvent = aggregatedEvent;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Object customFields) {
        this.customFields = customFields;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(Long timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public String timeString() {
        return Utils.getTimeAgo(getTimeStampCreated(), BaseApplication.getInstance().getApplicationContext(), true);
    }

    public String distanceString() {
        return getDistanceToLocation() + "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getIdPrimary() {
        return idPrimary;
    }

    public void setIdPrimary(long idPrimary) {
        this.idPrimary = idPrimary;
    }

    public boolean isHasLocation() {
        return getLocation() != null;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLastLatitude() {
        try {
            return getLocation().getCoordinates().get(1);
        } catch (Exception e) {
            return latitude;
        }
    }

    public double getLastLongitude() {
        try {
            return getLocation().getCoordinates().get(0);
        } catch (Exception e) {
            return longitude;
        }
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAggregatedEventId() {
        return AggregatedEventId;
    }

    public void setAggregatedEventId(long aggregatedEventId) {
        AggregatedEventId = aggregatedEventId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

}
