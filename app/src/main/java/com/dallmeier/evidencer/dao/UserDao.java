package com.dallmeier.evidencer.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dallmeier.evidencer.model.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity... userEntities);

    @Query("DELETE FROM user")
    void delete();

    @Query("SELECT * FROM user")
    UserEntity getUserInfo();

    @Query("SELECT * FROM user WHERE idUser = :id")
    UserEntity searchById(int id);

    @Update
    void update(UserEntity userEntity);

    default void insertOrUpdate(UserEntity item) {
        UserEntity itemsFromDB = searchById(item.getIdUser());
        if (itemsFromDB == null) {
            insertUser(item);
        } else {
            update(item);
        }
    }
}
