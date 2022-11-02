package com.dallmeier.evidencer.listener;

import android.app.Dialog;

public interface IAudioDialog {
    void clickSaveAudio(Dialog dialog);
    void clickRecordAudio(Dialog dialog, int task);
}
