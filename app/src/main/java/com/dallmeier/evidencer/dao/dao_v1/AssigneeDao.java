package com.dallmeier.evidencer.dao.dao_v1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.incident_response.StateEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface AssigneeDao {
    @Insert
    void insertAssignee(AssigneeEntity... assigneeEntities);

    @Query("DELETE FROM assignee")
    void delete();

    @Query("SELECT * FROM assignee")
    List<AssigneeEntity> getStates();

    @Query("SELECT * FROM assignee WHERE id = :id")
    AssigneeEntity searchById(long id);

    @Insert(onConflict = REPLACE)
    void insertAssignees(List<AssigneeEntity> assigneeEntities);

    @Update
    void update(AssigneeEntity assigneeEntity);

    default void insertOrUpdate(AssigneeEntity item) {
        AssigneeEntity itemsFromDB = searchById(item.getId());
        if (itemsFromDB != null) {
            insertAssignee(item);
        } else {
            update(item);
        }
    }

}
