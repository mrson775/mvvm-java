package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaDetail {
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("number")
    @Expose
    private Integer number;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
