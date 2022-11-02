package com.dallmeier.evidencer.ui.incidents.evident;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.common.TabSelected;
import com.dallmeier.evidencer.databinding.ActivityEvidentBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshNumberTb;
import com.dallmeier.evidencer.listener.EventBus.RefreshTimeline;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.utils.ProgressHUD;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.dallmeier.evidencer.common.Statics.AGG_EVENT_ID;

@AndroidEntryPoint
public class EvidentActivity extends BaseActivity<EvidentViewModel, ActivityEvidentBinding> implements View.OnClickListener {
    public static void toActivity(Context context, long id) {
        Intent intent = new Intent(context, EvidentActivity.class);
        intent.putExtra(AGG_EVENT_ID, id);
        context.startActivity(intent);
    }

    private ActivityEvidentBinding mBinding;
    private ImageAdapter mAdapter;
    private IncidentEntity incident;
    private String currentPhotoPath;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evident;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        mBinding.setEvident(this);
        initViews();
        liveImages();
        liveUploads();
    }

    private void liveUploads() {
        mViewModel.getResponseUpload().observe(this, this::responseUpload);
    }

    private void responseUpload(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (mProgressHUD != null) {
                mProgressHUD.cancel();
            }
            Toast.makeText(this, getString(R.string.success_upload), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().postSticky(new RefreshTimeline());
            /**
             * refresh count of evident  of the tab selected
             */
            mViewModel.updateCountEvidents(mViewModel.getAppDatabase(), incident.getId());
            finish();
        } else {
            if (mProgressHUD != null) {
                mProgressHUD.cancel();
            }
            Toast.makeText(this, getString(R.string.un_success_upload), Toast.LENGTH_SHORT).show();
        }
    }

    private void liveImages() {
        mViewModel.getAllImages();
        if (mProgressHUD == null) {
            mProgressHUD = ProgressHUD.show(this, getString(R.string.loading), true, true, null);
        }
        mViewModel.getImagesLive().observe(this, this::notifyDataList);
    }

    private void notifyDataList(List<ImageEntity> images) {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.cancel();
            mProgressHUD = null;
        }
        if (mAdapter == null) {
            mAdapter = new ImageAdapter(this);
            //   mAdapter.setItemClickListener(this);
            mBinding.imagesRcv.setAdapter(mAdapter);
        }
        mAdapter.updateReceiptsList(images);
        mAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        incident = SharedPrefUtil.getInstance().getIncident();
        mBinding.toolbar.tvTitle.setText(getString(R.string.tool_bar_galery));
        mBinding.toolbar.imgBack.setOnClickListener(this);
        mBinding.toolbar.imgCamera.setOnClickListener(this);
        mBinding.toolbar.imgVideo.setOnClickListener(this);

    }

    public void onUpload() {
        if (mAdapter.getPostListSelected().size() > 0) {
            if (mProgressHUD == null) {
                mProgressHUD = ProgressHUD.show(this, getString(R.string.upload), true, true, null);
            }
            for (int i = 0; i <= mAdapter.getPostListSelected().size() - 1; i++) {
                mViewModel.setUpload(mAdapter.getPostListSelected().get(i), (int) incident.getAggregatedEvent().getId(), mAdapter.getPostListSelected().size() - i);
                Log.d("getFilePathFromURI", mAdapter.getPostListSelected().get(i).getImage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                onBackPressed();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.img_camera:
                dispatchTakePictureIntent();
                break;
            case R.id.img_video:
                dispatchTakeVideoIntent();
                break;

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Utils.createImageFile(this);
                currentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                captureResult.launch(takePictureIntent);
            }
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File videoFile = null;
            try {
                videoFile = Utils.createImageFileVideo(this);
                currentPhotoPath = videoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName() + ".provider",
                        videoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                videoResult.launch(takePictureIntent);
            }
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> captureResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (mProgressHUD == null) {
                            mProgressHUD = ProgressHUD.show(EvidentActivity.this, getString(R.string.upload), true, true, null);
                        }
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setImage(currentPhotoPath);
                        imageEntity.setTitle("");
                        mViewModel.setUpload(imageEntity, (int) incident.getAggregatedEvent().getId(), 1);
                    }
                }
            });
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> videoResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (mProgressHUD == null) {
                            mProgressHUD = ProgressHUD.show(EvidentActivity.this, getString(R.string.upload), true, true, null);
                        }
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setImage(currentPhotoPath);
                        imageEntity.setTitle("");
                        mViewModel.setUpload(imageEntity, incident.getAggregatedEvent().getId(), 1);
                    }
                }
            });

}