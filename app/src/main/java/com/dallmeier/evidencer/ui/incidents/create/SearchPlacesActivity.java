package com.dallmeier.evidencer.ui.incidents.create;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseActivity;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.databinding.ActivitySearchPlacesBinding;
import com.dallmeier.evidencer.model.UserEntity;
import com.dallmeier.evidencer.model.place.Places;
import com.dallmeier.evidencer.utils.IncidentOSMOverlay;
import com.dallmeier.evidencer.utils.LocationManagerUtils;
import com.dallmeier.evidencer.utils.MapUtils;
import com.dallmeier.evidencer.utils.Utils;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchPlacesActivity extends BaseActivity<IncidentCreateViewModel, ActivitySearchPlacesBinding> implements TextWatcher, PlacesClickListener, View.OnClickListener {
    public static void toActivity(Context context) {
        Intent intent = new Intent(context, SearchPlacesActivity.class);
        context.startActivity(intent);
    }

    private ActivitySearchPlacesBinding mBinding;
    private MapView mMapView;
    private IncidentOSMOverlay mOsmOverlay;
    private PlacesAdapter mAdapter;
    private Places places;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = getViewDataBinding();
        mBinding.setSearchPlacesActivity(this);
        initMap();
        initListener();
        livePlaces();
        mBinding.setSearchPlacesActivity(this);
    }

    private void livePlaces() {
        mViewModel.getPlacesLive().observe(this, this::notifyDataList);
    }

    private void notifyDataList(List<Places> places) {
        if (places.size() == 0) {
            mBinding.placesRcv.setVisibility(View.GONE);
        } else {
            mBinding.placesRcv.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new PlacesAdapter(this);
                mAdapter.setItemClickListener(this);
                mBinding.placesRcv.setAdapter(mAdapter);
            }
            mAdapter.updateReceiptsList(places);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initListener() {
        mBinding.toolbar.searchEdt.addTextChangedListener(this);
        mBinding.toolbar.imgBack.setOnClickListener(this);
    }

    private void initMap() {
        mMapView = MapUtils.createMapView(this, 2, 18, mBinding.mapContainer);
        Drawable defaultMarker = Utils.getIcon(this, R.drawable.marker_default);
        mOsmOverlay = new IncidentOSMOverlay(defaultMarker, this, mMapView);
        UserEntity userEntity = mViewModel.appDatabase().userDao().getUserInfo();
        try {
            MapUtils.zoomToCurrentLocation(this, mMapView, new GeoPoint(userEntity.getLatitude(), userEntity.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mViewModel.cancelCountdown();
        mViewModel.loadPlacesCountDown(s.toString());
    }

    @Override
    public void onClick(View view, int pos, List<Places> postList) {
        places = postList.get(pos);
        mOsmOverlay.clearOverlay();
        MapUtils.addMarkerWithCreateIncident(BaseApplication.getInstance().getApplicationContext(),
                mOsmOverlay, mMapView, postList.get(pos).getLat(), postList.get(pos).getLon());
        //reset search view
        mBinding.toolbar.searchEdt.setText("");
        mBinding.placesRcv.setVisibility(View.GONE);
        mAdapter.getPostList().clear();
        mAdapter.notifyDataSetChanged();
        Utils.hideSoftKeyboard(this);
    }

    public void callBackResult() {
        Intent returnIntent = new Intent();
        if (places != null) {
            returnIntent.putExtra(Statics.PLACES_LON, places.getLon());
            returnIntent.putExtra(Statics.PLACES_LAT, places.getLat());
            returnIntent.putExtra(Statics.ADDRESS, places.getDisplayName());
            setResult(Activity.RESULT_OK, returnIntent);
            this.finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LocationManagerUtils.sLocationService.checkLocationService(this)) {
            mMapView.onPause();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_places;
    }

    @Override
    public int getBindingViewModel() {
        return BR.viewModelCallback;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocationManagerUtils.sLocationService.checkLocationService(this)) {
            mMapView.onResume();
        }
    }

    public void onBack() {
        finish();
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.img_back:
                onBack();
                break;
        }
    }
}