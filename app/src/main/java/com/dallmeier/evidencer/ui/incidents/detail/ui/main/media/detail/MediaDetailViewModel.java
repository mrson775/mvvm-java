package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.evident.media.MetaDataEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class MediaDetailViewModel extends BaseViewModel<MediaDetailRepository> {
    private LiveData<List<MetaDataEntity>> entityMetadata = new MutableLiveData<List<MetaDataEntity>>();
    private final AppDatabase appDatabase;
    private final Picasso picasso;

    @Inject
    public MediaDetailViewModel(MediaDetailRepository mRepository, AppDatabase appDatabase, Picasso picasso) {
        super(mRepository);
        this.appDatabase = appDatabase;
        this.picasso = picasso;
    }

    public void initMedia(String idMedia) {
        entityMetadata = appDatabase.metaDataDao().searchByIdLive(idMedia);
    }

    public void getMetaData(String idMedia) {
        repository.getMetaData(idMedia);
    }

    public LiveData<List<MetaDataEntity>> getEntityMetadata() {
        if (entityMetadata == null) {
            entityMetadata = new MutableLiveData<>();
        }
        return entityMetadata;
    }

    public Picasso picasso() {
        return picasso;
    }
}
