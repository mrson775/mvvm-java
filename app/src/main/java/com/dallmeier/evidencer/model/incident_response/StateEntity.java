package com.dallmeier.evidencer.model.incident_response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dallmeier.evidencer.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "incident_state")
public class StateEntity {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @ColumnInfo(name = "tmpCheck", defaultValue = "false")
    private boolean tmpCheck;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTmpCheck() {
        return tmpCheck;
    }

    public void setTmpCheck(boolean tmpCheck) {
        this.tmpCheck = tmpCheck;
    }
}
