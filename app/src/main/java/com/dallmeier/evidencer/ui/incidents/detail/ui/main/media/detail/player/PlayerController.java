package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;

public class PlayerController implements IPlayer {
    private ExoPlayer mPlayer;
    private StyledPlayerView mPlayerView;
    private Context mContext;
    private long mCurrentMillis;
    DataSource.Factory dataSourceFactory;
    DefaultRenderersFactory renderersFactory;
    private DefaultTrackSelector trackSelector;

    public PlayerController(StyledPlayerView mPlayerView, Context mContext) {
        this.mPlayerView = mPlayerView;
        this.mContext = mContext;
    }

    @Override
    public void inItPlayer() {
        if (mPlayer == null) {
            dataSourceFactory = ExoPlayerUtil.getDataSourceFactory(/* context= */ mContext);
            RenderersFactory renderersFactory =
                    ExoPlayerUtil.buildRenderersFactory(/* context= */ mContext, true);
            MediaSourceFactory mediaSourceFactory =
                    new DefaultMediaSourceFactory(dataSourceFactory)
                            .setAdViewProvider(mPlayerView);
            trackSelector = new DefaultTrackSelector(/* context= */ mContext);
            mPlayer =
                    new SimpleExoPlayer.Builder(/* context= */ mContext, renderersFactory)
                            .setMediaSourceFactory(mediaSourceFactory)
                            .setTrackSelector(trackSelector)
                            .build();
            mPlayerView.setPlayer(mPlayer);

        }
    }

    @Override
    public void buildMediaSource(Uri mUri) {
        // Set the media item to be played.
        mPlayer.setMediaItem(MediaItem.fromUri(mUri));
        // Prepare the player.
        mPlayer.prepare();
    }

    @Override
    public void releasePlayer() {
        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
        if (mPlayer == null) {
            return;
        }
        mCurrentMillis = mPlayer.getCurrentPosition();
        mPlayer.release();
        mPlayer = null;

    }

}
