package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categorization {
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("originator")
    @Expose
    private Originator originator;
    @SerializedName("previewimage")
    @Expose
    private String previewimage;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Originator getOriginator() {
        return originator;
    }

    public void setOriginator(Originator originator) {
        this.originator = originator;
    }

    public String getPreviewimage() {
        return previewimage;
    }

    public void setPreviewimage(String previewimage) {
        this.previewimage = previewimage;
    }

}