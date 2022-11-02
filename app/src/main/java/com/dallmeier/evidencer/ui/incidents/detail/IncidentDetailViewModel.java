package com.dallmeier.evidencer.ui.incidents.detail;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.base.IUpdateEvidents;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.CommentDto;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;
import com.dallmeier.evidencer.utils.audio.AudioRecorder;
import com.google.gson.JsonObject;

import java.io.IOException;

import javax.inject.Inject;

public class IncidentDetailViewModel extends BaseViewModel<IncidentDetailRepository> {
    private MutableLiveData<JsonObject> responseUpload = new MutableLiveData<JsonObject>();
    private MutableLiveData<JsonObject> responseComment = new MutableLiveData<JsonObject>();
    private final AudioRecorder mAudioRecorder;
    private final FileMeta mFileMeta;
    private final Location location;
    private final Handler mHandler;
    private AppDatabase appDatabase;
    private IUpdateEvidents iUpdateEvidents;

    @Inject
    public IncidentDetailViewModel(IncidentDetailRepository repository, AudioRecorder audioRecorder, FileMeta mFileMeta, Location location, Handler mHandler, AppDatabase appDatabase) {
        super(repository);
        this.mAudioRecorder = audioRecorder;
        this.mFileMeta = mFileMeta;
        this.location = location;
        this.mHandler = mHandler;
        this.appDatabase = appDatabase;
        this.iUpdateEvidents = this;
    }

    public Handler getHandel() {
        return mHandler;
    }

    public void initPath() {
        mAudioRecorder.initPath(BaseApplication.getInstance().getString(R.string.path_sd));
    }

    public void setComment(CommentDto dto) {
        repository.createNewComment(dto, getResponseComment());
    }

    /**
     * @return List<Incident>
     */
    public MutableLiveData<JsonObject> getResponseComment() {
        if (responseComment == null) {
            responseComment = new MutableLiveData<>();
        }
        return responseComment;
    }

    public void recordAudio(int task) throws IOException {
        if (task == Statics.RECORDING) {
            mAudioRecorder.recordAudio();
        } else {
            mAudioRecorder.stopRecorder();
        }
    }

    /*
     * Response after upload image/video
     *
     * @return JsonObject
     */
    public MutableLiveData<JsonObject> getResponseUpload() {
        if (responseUpload == null) {
            responseUpload = new MutableLiveData<>();
        }
        return responseUpload;
    }

    public void setUpload(ImageEntity imageEntity, long AggId, int size) {
        repository.uploadIml(getResponseUpload(), imageEntity, AggId, mFileMeta, location, size);
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void updateCountEvidents(AppDatabase appDatabase, long incidentId) {
        this.iUpdateEvidents.updateEvidents(appDatabase, incidentId);
    }
}
