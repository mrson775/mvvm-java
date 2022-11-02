package com.dallmeier.evidencer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssigneeDto {
    @SerializedName("combine")
    @Expose
    private String combine;
    @SerializedName("criterias")
    @Expose
    private List<Criteria> criterias = null;

    public String getCombine() {
        return combine;
    }

    public void setCombine(String combine) {
        this.combine = combine;
    }

    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        this.criterias = criterias;
    }
}
