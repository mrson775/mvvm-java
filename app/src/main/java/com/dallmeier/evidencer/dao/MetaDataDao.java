package com.dallmeier.evidencer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.evident.media.MetaDataEntity;

import java.util.List;

@Dao
public interface MetaDataDao {
    @Insert
    void insertMetaData(MetaDataEntity... metaDataEntities);

    @Query("DELETE FROM meta_data")
    void delete();

    @Query("SELECT * FROM meta_data WHERE id like :id")
    LiveData<List<MetaDataEntity>> searchByIdLive(String id);

    @Query("SELECT * FROM meta_data WHERE id like :id")
    List<MetaDataEntity> searchById(String id);

    @Update
    void update(MetaDataEntity metaDataEntity);

    default void insertOrUpdate(MetaDataEntity item) {
        List<MetaDataEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertMetaData(item);
        } else {
            update(item);
        }
    }
}
