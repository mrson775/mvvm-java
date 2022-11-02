package com.dallmeier.evidencer.ui.incidents.detail.ui.main.timeline;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.model.AggregatedEventId;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.utils.audio.AudioRecorder;

import java.util.List;

import javax.inject.Inject;

public class TimelineViewModel extends BaseViewModel<TimelineRepository> {
    private MutableLiveData<List<MediaAttachment>> evListLiveData = new MutableLiveData<List<MediaAttachment>>();
    private Handler mHandler;
    private AudioRecorder audioRecorder;

    @Inject
    public TimelineViewModel(Handler mHandler, TimelineRepository mRepository, AudioRecorder audioRecorder) {
        super(mRepository);
        this.mHandler = mHandler;
        this.audioRecorder = audioRecorder;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void getEvidents(long aggregatedEventId) {
        repository.getEvidents(aggregatedEventId);
    }

    public void getMediaAttachments(AggregatedEventId aggregatedEventId, int page, int size, String sort) {
        repository.getMediaAttachments(aggregatedEventId, page, size, sort, getMediaListLiveData());
    }

    public MutableLiveData<List<MediaAttachment>> getMediaListLiveData() {
        if (evListLiveData == null) {
            evListLiveData = new MutableLiveData<>();
        }
        return evListLiveData;
    }

    public AudioRecorder audioRecorder() {
        return audioRecorder;
    }
}