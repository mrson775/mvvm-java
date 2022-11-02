package com.dallmeier.evidencer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.ImageEntity;

import java.util.List;

@Dao
public interface ImageSdDao {
    @Insert
    void insertIncident(ImageEntity... imageEntities);

    @Query("DELETE FROM images_sdcard")
    void delete();

    @Query("SELECT * FROM images_sdcard")
    LiveData<List<ImageEntity>> getImageEntitiesLive();

    @Query("SELECT * FROM images_sdcard")
    List<ImageEntity> getImageEntities();

    @Query("SELECT * FROM images_sdcard WHERE isSelected=1")
    List<ImageEntity> getImagesSelected();

    @Query("SELECT * FROM images_sdcard WHERE id = :id")
    List<ImageEntity> searchById(long id);

    @Update
    void update(ImageEntity imageEntity);

    default void insertOrUpdate(ImageEntity item) {
        List<ImageEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
        } else {
            update(item);
        }
    }

    default void insertSingle(ImageEntity item) {
        List<ImageEntity> itemsFromDB = searchById(item.getId());
        if (itemsFromDB.isEmpty()) {
            insertIncident(item);
        }
    }
}
