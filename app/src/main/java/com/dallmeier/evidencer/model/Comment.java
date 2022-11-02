package com.dallmeier.evidencer.model;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("timeStampCreated")
    @Expose
    private long timeStampCreated;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("incidentId")
    @Expose
    private long incidentId;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public long getId() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }

    public String getUserPost() {
        return Utils.getTimeAgo(timeStampCreated, BaseApplication.getInstance().getApplicationContext(), false) +
                " " +  BaseApplication.getInstance().getApplicationContext().getString(R.string.by) + " " + username;
    }
}
