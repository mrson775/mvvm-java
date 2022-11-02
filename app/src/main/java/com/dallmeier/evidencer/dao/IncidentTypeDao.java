package com.dallmeier.evidencer.dao;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;

import java.util.List;

@Dao
public interface IncidentTypeDao {
    @Insert
    void insertIncident(IncidentTypeEntity... incident);

    @Query("DELETE FROM incident_type")
    void delete();

    @Query("SELECT * FROM incident_type")
    LiveData<List<IncidentTypeEntity>> getIncidentTypeLive();

    @Query("SELECT description FROM incident_type")
    LiveData<List<String>> getDescriptionsLive();

    @Query("SELECT * FROM incident_type")
    List<IncidentTypeEntity> getEvidentTypes();

    @Query("SELECT * FROM incident_type WHERE id = :id")
    List<IncidentTypeEntity> searchById(long id);

    @Query("SELECT * FROM incident_type WHERE id = :id")
    LiveData<List<IncidentTypeEntity>> searchByIdLive(long id);

    @Update
    void update(IncidentTypeEntity incidentTypeEntity);

    default void insertOrUpdate(IncidentTypeEntity item) {
        List<IncidentTypeEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
        } else {
            update(item);
        }
    }

    default void insertSingle(IncidentTypeEntity item) {
        List<IncidentTypeEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
            Log.d("itemsFromDB", "itemsFromDB");
        }
    }
}
