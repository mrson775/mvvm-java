package com.dallmeier.evidencer.dao.dao_v1;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.incident_response.IncidentEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface IncidentDao {
    @Insert(onConflict = IGNORE)
    void insertIncident(IncidentEntity... incident);

    @Insert(onConflict = REPLACE)
    void insertAllIncident(List<IncidentEntity> cidVos);

    @Query("DELETE FROM incident")
    void delete();

    @Query("SELECT * FROM incident order by timeStampCreated desc")
    LiveData<List<IncidentEntity>> getIncidentsLive();

    @Query("SELECT * FROM incident")
    List<IncidentEntity> getIncidents();

    @Query("SELECT * FROM incident WHERE  incidentNbo LIKE '%' || :title || '%'order by timeStampCreated desc")
    List<IncidentEntity> getIncidentsByTitle(String title);

    @Query("SELECT * FROM incident WHERE  incidentNbo LIKE '%' || :title || '%' order by timeStampCreated desc")
    List<IncidentEntity> getIncidentsByTitleByCurrentUser(String title);

    @Query("SELECT * FROM incident WHERE id = :id")
    List<IncidentEntity> searchById(long id);

    @Query("SELECT * FROM incident WHERE id = :id")
    IncidentEntity getIncidentById(long id);

    @Update
    void update(IncidentEntity incident);

    default void insertOrUpdate(IncidentEntity item) {
        List<IncidentEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
        } else {
            update(item);
        }
    }

    default void insertSingle(IncidentEntity item) {
        List<IncidentEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
        }
    }
}
