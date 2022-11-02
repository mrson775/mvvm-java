package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.model.AggregatedEventId;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

public class MediaRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<List<MediaAttachment>> liveMutableLiveData;

    @Inject
    public MediaRepository(ApiService apiService, Gson gson) {
        super(apiService, gson);
    }

    public void getEvidents(long aggregatedEventId) {
        ApiTask.execute(() -> mService.getEvidents(aggregatedEventId), ApiTaskType.EVIDENTS, this);
    }

    public void getMediaAttachments(AggregatedEventId aggregatedEventId, int page, int size, String sort, MutableLiveData<List<MediaAttachment>> mediaAttachments) {
        this.liveMutableLiveData = mediaAttachments;
        ApiTask.execute(() -> mService.getMediaAttachment(aggregatedEventId, page, size, sort), ApiTaskType.MEDIA_ATTACHMENT, this);
    }

    /**
     * handleErrorAddSender
     *
     * @param task ApiTask
     */
    private void handleErrorCode(ApiTask task) {

    }

    private void passStringErr(CodeRequestResult codeRequestResult) {
        switch (codeRequestResult.getErrCode()) {
        }

    }

    @Override
    public boolean onResponse(ApiTask task) {
        if (task.getResponse() != null) {
            if (task.getResponse().code() == ApiResponseCode.SUCCESS) {
                switch (task.getType()) {
                    case MEDIA_ATTACHMENT:
                        JsonObject jsonObject = (JsonObject) task.getResponse().body();
                        assert jsonObject != null;
                        JsonArray mResArr = jsonObject.getAsJsonArray("content");
                        Type type = new TypeToken<List<MediaAttachment>>() {
                        }.getType();
                        List<MediaAttachment> mediaAttachments = gson.fromJson(mResArr, type);
                        liveMutableLiveData.postValue(mediaAttachments);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case MEDIA_ATTACHMENT:
                        handleErrorCode(task);
                        break;
                }
            }
        }
        return false;
    }
}