package com.dallmeier.evidencer.ui.incidents.detail;

import static android.Manifest.permission.RECORD_AUDIO;
import static com.dallmeier.evidencer.utils.Utils.sanitizePath;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.common.TabSelected;
import com.dallmeier.evidencer.databinding.ActivityIncidentDetailBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshComment;
import com.dallmeier.evidencer.listener.EventBus.RefreshNumberTb;
import com.dallmeier.evidencer.listener.EventBus.RefreshTimeline;
import com.dallmeier.evidencer.listener.IAudioDialog;
import com.dallmeier.evidencer.listener.ICommentDialog;
import com.dallmeier.evidencer.model.CommentDto;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.AggregatedEvent;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.ui.incidents.detail.ui.main.SectionsPagerAdapter;
import com.dallmeier.evidencer.ui.incidents.evident.EvidentActivity;
import com.dallmeier.evidencer.utils.IncidentOSMOverlay;
import com.dallmeier.evidencer.utils.MapUtils;
import com.dallmeier.evidencer.utils.ProgressHUD;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.osmdroid.views.MapView;

import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IncidentDetailActivity extends BaseActivity<IncidentDetailViewModel, ActivityIncidentDetailBinding> implements View.OnClickListener, ICommentDialog, IAudioDialog {
    private ActivityIncidentDetailBinding mBinding;
    private IncidentEntity currentIncident;
    private Runnable mRunnable;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IncidentDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_incident_detail;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mBinding = getViewDataBinding();
        mBinding.setDetailIncident(this);
        mViewModel.initPath();
        mRunnable = () -> runOnUiThread(() -> {
            getBundle();
            initViews();
            initViewpager();
            initMap();
            liveComment();
            liveUploads();
        });
        mViewModel.getHandel().postDelayed(mRunnable, 150);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mViewModel.getHandel() != null && mRunnable != null) {
            mViewModel.getHandel().removeCallbacks(mRunnable);
        }
    }

    private void initViews() {
        mBinding.toolbar.imgBack.setOnClickListener(this);
        mBinding.toolbar.tvTitle.setText(currentIncident.getIncidentNbo() + "(" + currentIncident.timeString() + ")");
    }

    private void initViewpager() {
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), currentIncident.getId(), mViewModel.appDatabase());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPageTransformer(false, (page, position) -> transformPageCustom(page));
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager, false);
    }

    private void initMap() {
        MapView mMapView = MapUtils.createMapView(this, 2, 18, mBinding.mapContainer);
        Drawable defaultMarker = Utils.getIcon(this, R.drawable.marker_default);
        IncidentOSMOverlay mOsmOverlay = new IncidentOSMOverlay(defaultMarker, this, mMapView);
        mOsmOverlay.clearOverlay();
        MapUtils.addMarkerSingleWithAllUser(this,
                mOsmOverlay, mMapView, currentIncident);
    }

    private void getBundle() {
        currentIncident = SharedPrefUtil.getInstance().getIncident();
    }

    private void liveComment() {
        mViewModel.getResponseComment().observe(this, this::notifyComment);
    }

    private void liveUploads() {
        mViewModel.getResponseUpload().observe(this, this::notifyMedia);
    }

    private void notifyMedia(JsonObject jsonObject) {
        if (jsonObject != null) {
            mProgressHUD.cancel();
            Toast.makeText(this, getString(R.string.success_upload), Toast.LENGTH_SHORT).show();
            //update count of evident of the incident
            mViewModel.updateCountEvidents(mViewModel.getAppDatabase(), currentIncident.getId());
            EventBus.getDefault().post(new RefreshTimeline());
        }
    }

    private void notifyComment(JsonObject jsonObject) {
        Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new RefreshComment());
    }

    private void transformPageCustom(View page) {
        page.setAlpha(0f);
        page.setVisibility(View.VISIBLE);
        // Start Animation for a short period of time
        page.animate()
                .alpha(1f)
                .setDuration(50);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onBack() {
        onBackPressed();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.img_back:
                onBack();
                break;
        }
    }

    public void showHideViewFunction() {
        if (mBinding.evidentFunction.incidentDetailActivityFunction.getVisibility() == View.VISIBLE) {
            mBinding.evidentFunction.incidentDetailActivityFunction.setVisibility(View.GONE);
        } else {
            mBinding.evidentFunction.incidentDetailActivityFunction.setVisibility(View.VISIBLE);
        }
    }

    public void intentEvidentCreate() {
        Gson gson = new Gson();
        AggregatedEvent aggregatedEvent = gson.fromJson(currentIncident.AggregatedEvent(), AggregatedEvent.class);
        EvidentActivity.toActivity(this, aggregatedEvent.getId());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void intentCreateComment() {
        Utils.showCommentDialog(this, this);
    }

    public void intentRecordAudio() {
        if (hasPermissionSingle(RECORD_AUDIO)) {
            Utils.showRecordAudioDialog(this, this);
        } else {
            requestPermissionAudio();
        }
    }

    @Override
    public void clickYes(Dialog dialog, String content) {
        CommentDto commentDto = new CommentDto(content, currentIncident.getId());
        mViewModel.setComment(commentDto);
        dialog.dismiss();
    }

    @Override
    public void clickSaveAudio(Dialog dialog) {
        dialog.dismiss();
        Gson gson = new Gson();
        AggregatedEvent aggregatedEvent = gson.fromJson(currentIncident.AggregatedEvent(), AggregatedEvent.class);
        ImageEntity imageEntity = new ImageEntity();

        imageEntity.setImage(sanitizePath(BaseApplication.getInstance().getApplicationContext().getString(R.string.path_sd)));
        imageEntity.setTitle("");
        if (mProgressHUD == null) {
            mProgressHUD = ProgressHUD.show(IncidentDetailActivity.this, getString(R.string.upload), true, true, null);
        }
        mViewModel.setUpload(imageEntity, aggregatedEvent.getId(), 1);
    }

    @Override
    public void clickRecordAudio(Dialog dialog, int task) {
        try {
            mViewModel.recordAudio(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void updatePageTitle(RefreshNumberTb refreshNumberTb) {
        updateTabBar(refreshNumberTb.getPosition());
    }

    /**
     * Update count of item at current tab
     *
     * @param position int
     */
    private void updateTabBar(int position) {
        TabLayout.Tab tab = tabs.getTabAt(position);
        tab.setText(sectionsPagerAdapter.customTitle(position));
    }
}