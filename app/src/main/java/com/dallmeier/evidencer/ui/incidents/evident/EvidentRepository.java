package com.dallmeier.evidencer.ui.incidents.evident;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.base.IUploadMedia;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;

public class EvidentRepository extends BaseRepository implements ApiResponseCallback {
    MutableLiveData<JsonObject> responseUpload;
    Gson gson;
    private int sizeUpload = 0;
    private final IUploadMedia iUploadMedia;
    private AppDatabase appDatabase;

    @Inject
    public EvidentRepository(ApiService apiService, Gson gson, AppDatabase appDatabase) {
        super(apiService, gson);
        this.gson = gson;
        iUploadMedia = this;
        this.appDatabase = appDatabase;
    }

    public void uploadIml(MutableLiveData<JsonObject> mutableLiveUpload, ImageEntity imageEntity, long AggId, FileMeta mFileMeta, Location location, int size) {
        sizeUpload = size;
        this.responseUpload = mutableLiveUpload;
        iUploadMedia.upload(appDatabase, imageEntity, AggId, mFileMeta, location);
    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case UPLOAD:
                        JsonObject jsonObject = (JsonObject) task.getResponse().body();
                        if (sizeUpload == 1)
                            responseUpload.postValue(jsonObject);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case UPLOAD:
                        responseUpload.postValue(null);
                        break;
                }
            }
        }
        return false;
    }
}