package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Uploader {
    @SerializedName("remotePersonId")
    @Expose
    private Integer remotePersonId;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getRemotePersonId() {
        return remotePersonId;
    }

    public void setRemotePersonId(Integer remotePersonId) {
        this.remotePersonId = remotePersonId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
