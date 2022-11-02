package com.dallmeier.evidencer.dao.dao_v1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.incident_response.StateEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StateDao {
    @Insert
    void insertState(StateEntity... stateEntities);

    @Query("DELETE FROM incident_State")
    void delete();

    @Query("SELECT * FROM incident_State")
    List<StateEntity> getStates();

    @Query("SELECT * FROM incident_State WHERE id = :id")
    StateEntity searchById(long id);

    @Query("SELECT * FROM incident_State WHERE tmpCheck ==1")
    StateEntity searchTmpCheck();

    @Insert(onConflict = REPLACE)
    void insertStates(List<StateEntity> stateEntities);

    @Update
    void update(StateEntity incidentTypeEntity);

    default void insertOrUpdate(StateEntity item) {
        StateEntity itemsFromDB = searchById(item.getId());
        if (itemsFromDB != null) {
            insertState(item);
        } else {
            update(item);
        }
    }

}
