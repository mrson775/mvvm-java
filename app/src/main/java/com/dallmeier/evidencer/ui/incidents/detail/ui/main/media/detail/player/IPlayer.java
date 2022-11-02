package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail.player;

import android.net.Uri;

public interface IPlayer {
    void inItPlayer();
    void buildMediaSource(Uri uri);
    void releasePlayer();
}
