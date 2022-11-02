package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.databinding.ActivityMediaDetailBinding;
import com.dallmeier.evidencer.model.evident.media.MetaDataEntity;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.media.detail.player.PlayerController;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.dallmeier.evidencer.common.Constant.DOMAIN;
import static com.dallmeier.evidencer.common.Statics.MEDIA_ID;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE_PNG;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_VIDEO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO_MPEG;
import static com.dallmeier.evidencer.common.Statics.URL_MEDIA;

@AndroidEntryPoint
public class MediaDetailActivity extends BaseActivity<MediaDetailViewModel, ActivityMediaDetailBinding> implements View.OnClickListener {
    public static void toActivity(Context context, String mediaId) {
        Intent intent = new Intent(context, MediaDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MEDIA_ID, mediaId);
        context.startActivity(intent);
    }

    private String mediaId;
    private ActivityMediaDetailBinding mBinding;
    private PlayerController mPlayerController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        initViews();
        getBundle();
        initMetaDataLive();
    }

    private void initPlayer() {
        mPlayerController = new PlayerController(mBinding.mediaDetailVideo, this);
        mPlayerController.inItPlayer();
        mPlayerController.buildMediaSource(Uri.parse(DOMAIN + URL_MEDIA + mediaId));
        if (Util.SDK_INT > 23) {
            mBinding.mediaDetailVideo.onResume();
        }
    }

    private void directMedia(String contentType) {
        switch (contentType) {
            case MIMETYPE_IMAGE_PNG:
            case MIMETYPE_IMAGE:
                mBinding.mediaDetailImg.setVisibility(View.VISIBLE);
                mBinding.mediaDetailVideo.setVisibility(View.GONE);
                mViewModel.picasso().load(DOMAIN + URL_MEDIA + mediaId).placeholder(R.drawable.progress_animation).error(R.drawable.ic_no_img).into(mBinding.mediaDetailImg);
                break;
            case MIMETYPE_VIDEO:
            case MIME_TYPE_AUDIO:
            case MIME_TYPE_AUDIO_MPEG:
                mBinding.mediaDetailImg.setVisibility(View.GONE);
                mBinding.mediaDetailVideo.setVisibility(View.VISIBLE);
                initPlayer();
                break;
        }
    }

    private void initViews() {
        mBinding.toolbar.imgBack.setOnClickListener(this);
        mBinding.toolbar.tvTitle.setText(getString(R.string.media_detail));
    }

    private void getBundle() {
        mediaId = getIntent().getStringExtra(MEDIA_ID);
        mViewModel.initMedia(mediaId);
        mViewModel.getMetaData(mediaId);
    }

    private void initMetaDataLive() {
        mViewModel.getEntityMetadata().observe(this, this::notifyDataList);
    }

    private void notifyDataList(List<MetaDataEntity> metaDataEntities) {
        if (metaDataEntities.size() == 0) {
            return;
        }
        mBinding.setMediaDetailEntity(metaDataEntities.get(0));
        directMedia(metaDataEntities.get(0).getContentType());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_media_detail;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        if (mPlayerController != null) {
            mPlayerController.releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mPlayerController != null) {
            mPlayerController.releasePlayer();
        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
        }
    }
}