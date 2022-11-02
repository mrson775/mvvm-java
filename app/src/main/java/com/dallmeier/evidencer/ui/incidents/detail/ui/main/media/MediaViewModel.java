package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.model.AggregatedEventId;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.utils.audio.AudioRecorder;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class MediaViewModel extends BaseViewModel<MediaRepository> {
    private MediaRepository mRepository;
    private MutableLiveData<List<MediaAttachment>> evListLiveData = new MutableLiveData<List<MediaAttachment>>();
    private final Handler mHandler;
    private final Picasso picasso;
    private AudioRecorder audioRecorder;

    @Inject
    public MediaViewModel(Handler mHandler, MediaRepository mRepository, Picasso picasso, AudioRecorder audioRecorder) {
        super(mRepository);
        this.mRepository = (MediaRepository) mRepository;
        this.mHandler = mHandler;
        this.picasso = picasso;
        this.audioRecorder = audioRecorder;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void getMediaAttachments(AggregatedEventId aggregatedEventId, int page, int size, String sort) {
        mRepository.getMediaAttachments(aggregatedEventId, page, size, sort, getMediaListLiveData());
    }

    public MutableLiveData<List<MediaAttachment>> getMediaListLiveData() {
        if (evListLiveData == null) {
            evListLiveData = new MutableLiveData<>();
        }
        return evListLiveData;
    }

    public Picasso picasso() {
        return picasso;
    }

    public AudioRecorder audioRecorder() {
        return audioRecorder;
    }
}