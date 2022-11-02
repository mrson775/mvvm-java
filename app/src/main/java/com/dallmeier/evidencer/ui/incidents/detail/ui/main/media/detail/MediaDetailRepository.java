package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail;

import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.evident.media.MetaDataEntity;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiServiceMap;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;

public class MediaDetailRepository extends BaseRepository implements ApiResponseCallback {
    private final Gson gson;
    private final AppDatabase appDatabase;

    @Inject
    public MediaDetailRepository(ApiService apiService, Gson gson, AppDatabase appDatabase) {
        super(apiService, gson);
        this.gson = gson;
        this.appDatabase = appDatabase;
    }


    public void getMetaData(String idMedia) {
        ApiTask.execute(() -> mService.getMetaMedia(idMedia), ApiTaskType.META_DATA, this);
    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCodeSender(ApiTask task) {

    }

    private void passStringErr(CodeRequestResult codeRequestResult) {
        switch (codeRequestResult.getErrCode()) {
        }

    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                //todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case META_DATA:
                        JsonObject jsonObject = (JsonObject) task.getResponse().body();
                        MetaDataEntity metaDataEntity = gson.fromJson(jsonObject, MetaDataEntity.class);
                        appDatabase.metaDataDao().insertOrUpdate(metaDataEntity);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case META_DATA:
                        handleErrorCodeSender(task);
                        break;
                }
            }
        }
        return false;
    }
}