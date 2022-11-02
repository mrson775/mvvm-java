package com.dallmeier.evidencer.di;


import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.network.ApiResponseCallback;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public abstract class BaseRepositoryModule {
    @Binds
    public abstract ApiResponseCallback biApiResponseCallback(BaseRepository baseRepository);
}
