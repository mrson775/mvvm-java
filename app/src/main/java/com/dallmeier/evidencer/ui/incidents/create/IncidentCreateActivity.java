package com.dallmeier.evidencer.ui.incidents.create;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.databinding.ActivityIncidentCreateBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshIncident;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.incident.IncidentTypeEntity;
import com.dallmeier.evidencer.model.incident.create.Incident;
import com.dallmeier.evidencer.utils.MapUtils;
import com.dallmeier.evidencer.utils.ProgressHUD;
import com.dallmeier.evidencer.utils.Utils;
import com.dallmeier.evidencer.utils.customview.CustomSpinnerAdapter;
import com.dallmeier.evidencer.utils.customview.DateTimeDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IncidentCreateActivity extends BaseActivity<IncidentCreateViewModel, ActivityIncidentCreateBinding> implements DialogInterface.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, IncidentCreateActivity.class);
        context.startActivity(intent);
    }

    private ActivityIncidentCreateBinding mBinding;
    private ArrayAdapter<String> adapter;
    private AssigneeAdapter adapterAssignee;
    private DateTimeDialog mDialog;
    private final List<IncidentTypeEntity> listIncidentTypeEntities = new ArrayList<>();
    private final List<String> listDescription = new ArrayList<>();
    private View thumbView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_incident_create;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        mBinding.setIncidentActivity(this);
        getIncidentType();
        initViews();
        mViewModel.initIncidentDto();
        liveCreateNewIncident();
        liveAssignee();
    }

    private void liveCreateNewIncident() {
        mViewModel.incidentNewLive().observe(this, this::notifyDataList);
    }

    private void liveAssignee() {
        initAssignee(mViewModel.assignees());
    }

    private void notifyDataList(Incident incident) {
        if (incident != null) {
            if (mProgressHUD != null) {
                mProgressHUD.cancel();
            }
            this.finish();
            this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            EventBus.getDefault().post(new RefreshIncident());
        }
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void initViews() {
        mBinding.toolbar.imgBack.setOnClickListener(this);
        mBinding.toolbar.tvTitle.setText(getString(R.string.new_incident));
        mDialog = new DateTimeDialog(this);
        mDialog.setButton(BUTTON_NEGATIVE, getString(R.string.cancel), this);
        mDialog.setButton(BUTTON_POSITIVE, getString(R.string.ok), this);
        mBinding.dateTimeEdt.setText(Utils.getDateNotIncludedToday(System.currentTimeMillis()));
        mBinding.typeOfIncidentSp.setOnItemSelectedListener(this);
        mBinding.assigneeSp.setOnItemSelectedListener(this);
        mBinding.criticalSb.setProgress(50);
        mBinding.criticalSb.setMax(100);
        thumbView = LayoutInflater.from(this).inflate(R.layout.layout_seekbar_thumb, null, false);
        mBinding.criticalSb.setThumb(Utils.getThumb(50, thumbView, this));
        mBinding.criticalSb.setOnSeekBarChangeListener(this);
        //get current location
        try {
            double currentLat = mViewModel.appDatabase().userDao().getUserInfo().getLatitude();
            double currentLon = mViewModel.appDatabase().userDao().getUserInfo().getLongitude();
            mBinding.latitudeEdt.setText(currentLat + "");
            mBinding.longitudeEdt.setText(currentLon + "");
            mBinding.addressEdt.setText(MapUtils.getAddressFromLatLon(this, currentLat, currentLon));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return mViewModel.appDatabase().userDao().getUserInfo().getUserName();
    }

    public void showCalendar() {
        mDialog.show();
        mDialog.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
        mDialog.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
    }

    private void getIncidentType() {
        getIncidentTypes(mViewModel.incidentTypes());
    }

    private void getIncidentTypes(List<IncidentTypeEntity> incidentTypeEntities) {
        listIncidentTypeEntities.clear();
        listDescription.clear();
        listIncidentTypeEntities.addAll(incidentTypeEntities);
        for (int i = 0; i < listIncidentTypeEntities.size(); i++) {
            listDescription.add(incidentTypeEntities.get(i).getDescription());
        }
        initTypeIncident(listDescription);
    }

    private void initTypeIncident(List<String> items) {
        if (adapter == null) {
            adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, items);
            mBinding.typeOfIncidentSp.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initAssignee(List<AssigneeEntity> items) {
        if (adapterAssignee == null) {
            adapterAssignee = new AssigneeAdapter(this, R.layout.spinner_item, items);
            mBinding.assigneeSp.setAdapter(adapterAssignee);
        } else {
            adapterAssignee.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                mBinding.dateTimeEdt.setText(Utils.getDateNotIncludedToday(mDialog.getTime()));
                mViewModel.getAggregatedEvent().setTimeStampMillis(mDialog.getTime());
                mViewModel.getIncident().setTimeStampCreated(mDialog.getTime());
                break;
        }
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> placeActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Double latitude = data.getDoubleExtra(Statics.PLACES_LAT, 0);
                        Double longitude = data.getDoubleExtra(Statics.PLACES_LON, 0);
                        String address = data.getStringExtra(Statics.ADDRESS);
                        mBinding.longitudeEdt.setText(longitude + "");
                        mBinding.latitudeEdt.setText(latitude + "");
                        mBinding.addressEdt.setText(address);
                    }
                }
            });

    public void intentSearchView() {
        SearchPlacesActivity.toActivity(this);
        Intent mainIntent = new Intent(this, SearchPlacesActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        placeActivityResult.launch(mainIntent);
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void inputIncidentDto() {
        if (mProgressHUD == null) {
            mProgressHUD = ProgressHUD.show(this, getString(R.string.waitting), true, true, null);
        }
        mViewModel.inputIncidentDto(Double.valueOf(Objects.requireNonNull(mBinding.longitudeEdt.getText()).toString()), Double.valueOf(Objects.requireNonNull(mBinding.latitudeEdt.getText()).toString()), mBinding.addressEdt.getText().toString());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBar.setThumb(Utils.getThumb(progress, thumbView, this));
        mViewModel.getAggregatedEvent().setCriticality(mBinding.criticalSb.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.type_of_incident_sp:
                if (listIncidentTypeEntities.size() > 0 && mViewModel.getIncident() != null && position < listIncidentTypeEntities.size())
                    mViewModel.getIncident().setIncidentTypeEntity(listIncidentTypeEntities.get(position));
                break;
            case R.id.assignee_sp:
                if (adapterAssignee.getAssignees().size() > 0) {
                    mViewModel.getIncident().setAssigneeId(adapterAssignee.getAssignees().get(position).getId());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBack() {
        onBackPressed();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.img_back:
                onBack();
                break;
            default:
                //todo some thing
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}