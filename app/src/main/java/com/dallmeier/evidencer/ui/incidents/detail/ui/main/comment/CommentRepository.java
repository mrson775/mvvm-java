package com.dallmeier.evidencer.ui.incidents.detail.ui.main.comment;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseRepository;
import com.dallmeier.evidencer.model.Comment;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiResponseCode;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.dallmeier.evidencer.network.CodeRequestResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CommentRepository extends BaseRepository implements ApiResponseCallback {
    private MutableLiveData<List<Comment>> liveDataComments;

    @Inject
    public CommentRepository(ApiService apiService, Gson gson) {
        super(apiService, gson);
    }

    public void getComments(long incidentId, MutableLiveData<List<Comment>> liveDataComments) {
        this.liveDataComments = liveDataComments;
        ApiTask.execute(() -> mService.getCommentOfIncident(incidentId), ApiTaskType.COMMENTS, this);
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
                    case COMMENTS:
                        List<Comment> commentEntities = (ArrayList<Comment>) task.getResponse().body();
                        liveDataComments.postValue(commentEntities);
                        break;
                }
            } else {
                //todo handle error code
                switch (task.getType()) {
                    case COMMENTS:
                        handleErrorCode(task);
                        break;
                }
            }
        }
        return false;
    }
}