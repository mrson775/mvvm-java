package com.dallmeier.evidencer.utils.audio;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.widget.Toast;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail.MediaDetailActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import static com.dallmeier.evidencer.common.Constant.DOMAIN;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE_PNG;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_VIDEO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO_MPEG;
import static com.dallmeier.evidencer.common.Statics.URL_MEDIA;
import static com.dallmeier.evidencer.utils.Utils.sanitizePath;

public class AudioRecorder implements IAudioComponent {

    private MediaRecorder recorder;
    private MediaPlayer mMediaPlayer;
    String path;

    @Inject
    public AudioRecorder() {
    }

    public void initPath(String path) {
        this.path = sanitizePath(path);
    }

    public void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    public void stopMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void stopRecorder() throws IOException {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
        } catch (RuntimeException e) {
        }
    }

    public void recordAudio() throws IOException {
        File file = new File(path);
        if (file.exists()) {
            if (file.delete()) {
                start();
            } else {
                System.out.println("file not Deleted");
            }
        } else {
            start();
        }
    }

    @Override
    public void play(MediaAttachment mediaAttachment, Activity context) {
        switch (mediaAttachment.getMimeType()) {
            case MIMETYPE_VIDEO:
            case MIMETYPE_IMAGE:
            case MIMETYPE_IMAGE_PNG:
            case MIME_TYPE_AUDIO:
            case MIME_TYPE_AUDIO_MPEG:
                playMedia(mediaAttachment, context);
                break;
            default:
                openFile(mediaAttachment, context);
        }
    }

    private void playMedia(MediaAttachment mediaAttachment, Activity activity) {
        if (mediaAttachment != null) {
            if (mediaAttachment.getUrl().startsWith(DOMAIN + URL_MEDIA)) {
                MediaDetailActivity.toActivity(BaseApplication.getInstance().getApplicationContext(), mediaAttachment.getUrl().replace(DOMAIN + URL_MEDIA, "").trim());
                activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, activity.getString(R.string.not_found_file), Toast.LENGTH_SHORT).show();
        }
    }

    private void openFile(MediaAttachment mediaAttachment, Activity activity) {
        if (mediaAttachment != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaAttachment.getUrl()));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(browserIntent);
            activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            Toast.makeText(activity, activity.getString(R.string.not_found_file), Toast.LENGTH_SHORT).show();
        }
    }
}
