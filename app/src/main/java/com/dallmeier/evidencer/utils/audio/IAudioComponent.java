package com.dallmeier.evidencer.utils.audio;

import android.app.Activity;

import com.dallmeier.evidencer.model.evident.MediaAttachment;

public interface IAudioComponent {
    void play(MediaAttachment mediaAttachment, Activity context);
}
