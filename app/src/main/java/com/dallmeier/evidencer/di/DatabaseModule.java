package com.dallmeier.evidencer.di;

import android.content.Context;

import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.dao.UserDao;
import com.dallmeier.evidencer.dao.dao_v1.AssigneeDao;
import com.dallmeier.evidencer.dao.dao_v1.IncidentDao;
import com.dallmeier.evidencer.dao.dao_v1.StateDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatabaseModule {
    @Singleton
    @Provides
    public AppDatabase database(@ApplicationContext Context context) {
        return AppDatabase.instance(context);
    }

    @Provides
    public IncidentDao incidentDao(AppDatabase appDatabase) {
        return appDatabase.incidentDao();
    }

    @Provides
    public AssigneeDao assigneeDao(AppDatabase appDatabase) {
        return appDatabase.assigneeDao();
    }

    @Provides
    public StateDao stateDao(AppDatabase appDatabase) {
        return appDatabase.stateDao();
    }

    @Provides
    public UserDao userDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }
}
