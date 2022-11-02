package com.dallmeier.evidencer.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dallmeier.evidencer.dao.dao_v1.AssigneeDao;
import com.dallmeier.evidencer.dao.dao_v1.IncidentDao;
import com.dallmeier.evidencer.dao.dao_v1.StateDao;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.UserEntity;
import com.dallmeier.evidencer.model.evident.media.MetaDataEntity;
import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.model.incident_response.StateEntity;


@Database(entities = {UserEntity.class, IncidentEntity.class, MetaDataEntity.class,
        IncidentTypeEntity.class, ImageEntity.class, StateEntity.class, AssigneeEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database = null;
    public abstract UserDao userDao();
    public abstract IncidentDao incidentDao();
    public abstract MetaDataDao metaDataDao();
    public abstract IncidentTypeDao incidentTypeDao();
    public abstract ImageSdDao imageSdDao();
    public abstract StateDao stateDao();
    public abstract AssigneeDao assigneeDao();
    public static synchronized AppDatabase instance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context,
                    AppDatabase.class, "evident-database").allowMainThreadQueries().build();
        }
        return database;
    }
}
