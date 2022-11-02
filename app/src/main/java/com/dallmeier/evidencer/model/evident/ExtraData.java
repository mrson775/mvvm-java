package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraData {
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

}
