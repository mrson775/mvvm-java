package com.dallmeier.evidencer.model.evident;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Preview {
    @SerializedName("numberAllEvidences")
    @Expose
    private Integer numberAllEvidences;
    @SerializedName("evidenceTypes")
    @Expose
    private List<EvidenceType> evidenceTypes = null;
    @SerializedName("thumbnailURL")
    @Expose
    private Object thumbnailURL;

    public Integer getNumberAllEvidences() {
        return numberAllEvidences;
    }

    public void setNumberAllEvidences(Integer numberAllEvidences) {
        this.numberAllEvidences = numberAllEvidences;
    }

    public List<EvidenceType> getEvidenceTypes() {
        return evidenceTypes;
    }

    public void setEvidenceTypes(List<EvidenceType> evidenceTypes) {
        this.evidenceTypes = evidenceTypes;
    }

    public Object getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(Object thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
