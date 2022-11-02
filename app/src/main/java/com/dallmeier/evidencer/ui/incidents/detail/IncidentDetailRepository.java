package com.dallmeier.evidencer.ui.incidents.detail;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.base.IUploadMedia;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.CommentDto;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;

public class IncidentDetailRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<JsonObject> responseComment;
    private MutableLiveData<JsonObject> responseUpload;
    private int sizeUpload = 0;
    private IUploadMedia iUploadMedia;
    private AppDatabase appDatabase;

    @Inject
    public IncidentDetailRepository(ApiService apiService, Gson gson, AppDatabase appDatabase) {
        super(apiService, gson);
        iUploadMedia = this;
        this.appDatabase = appDatabase;
    }

    public void createNewComment(CommentDto commentDto, MutableLiveData<JsonObject> liveData) {
        responseComment = liveData;
        ApiTask.execute(() -> mService.createComment(commentDto), ApiTaskType.CREATE_COMMENT, this);
    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCodeSender(ApiTask task) {

    }

    public void uploadIml(MutableLiveData<JsonObject> mutableLiveUpload, ImageEntity imageEntity, long AggId, FileMeta mFileMeta, Location location, int size) {
        sizeUpload = size;
        this.responseUpload = mutableLiveUpload;
        iUploadMedia.upload(appDatabase, imageEntity, AggId, mFileMeta, location);
    }

    private void passStringErr(CodeRequestResult codeRequestResult) {
        switch (codeRequestResult.getErrCode()) {
        }

    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS ||
                    task.getResponse().code() == ApiResponseCode.SUCCESS_CREATE) {
                JsonObject jsonObject = (JsonObject) task.getResponse().body();//todo handle success code
                /*   CodeRequestResult codeRequestResult = new CodeRequestResult();*/
                switch (task.getType()) {
                    case CREATE_COMMENT:
                        responseComment.postValue(jsonObject);
                        break;
                    case UPLOAD:
                        if (sizeUpload == 1)
                            responseUpload.postValue(jsonObject);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case CREATE_COMMENT:
                        responseComment.postValue(null);
                        break;
                    case UPLOAD:
                        responseUpload.postValue(null);
                        break;
                }
            }
        }
        return false;
    }
}