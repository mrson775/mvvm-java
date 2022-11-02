package com.dallmeier.evidencer.model.incident;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "incident_type")
public class IncidentTypeEntity {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("classType")
    @Expose
    private String classType;
    @SerializedName("timeStampCreated")
    @Expose
    private Integer timeStampCreated;
    @SerializedName("version")
    @Expose
    private Integer version;
    @Ignore
    @SerializedName("incidentCatalog")
    @Expose
    private IncidentCatalog incidentCatalog;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("code")
    @Expose
    private Integer code;
    @Ignore
    @SerializedName("customFields")
    @Expose
    private Object customFields;
    @Ignore
    @SerializedName("workflow")
    @Expose
    private Workflow workflow;

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

    public Integer getTimeStampCreated() {
        return timeStampCreated;
    }

    public void setTimeStampCreated(Integer timeStampCreated) {
        this.timeStampCreated = timeStampCreated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public IncidentCatalog getIncidentCatalog() {
        return incidentCatalog;
    }

    public void setIncidentCatalog(IncidentCatalog incidentCatalog) {
        this.incidentCatalog = incidentCatalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Object customFields) {
        this.customFields = customFields;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

}
