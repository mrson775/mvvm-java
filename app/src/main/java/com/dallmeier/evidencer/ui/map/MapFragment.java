package com.dallmeier.evidencer.ui.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseFragment;
import com.dallmeier.evidencer.databinding.FragmentMapBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshIncident;
import com.dallmeier.evidencer.listener.EventBus.SearchInput;
import com.dallmeier.evidencer.listener.EventBus.UpdateLocation;
import com.dallmeier.evidencer.model.UserEntity;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.ui.incidents.create.IncidentCreateActivity;
import com.dallmeier.evidencer.ui.incidents.detail.IncidentDetailActivity;
import com.dallmeier.evidencer.utils.EvidentOSMOverlay;
import com.dallmeier.evidencer.utils.IncidentOSMOverlay;
import com.dallmeier.evidencer.utils.LocationManagerUtils;
import com.dallmeier.evidencer.utils.MapUtils;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapFragment extends BaseFragment<FragmentMapBinding, MapViewModel> implements MapListener, EvidentOSMOverlay.OnMarkerTapListener {
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    private FragmentMapBinding mBinding;
    private MapView mMapView;
    private IncidentOSMOverlay mOsmOverlay;
    private Runnable mRunnable;
    private String input;

    @Override
    public int getBindingVariable() {
        return BR.viewModelCallback;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mBinding.setFragmentCallback(this);
        LocationManagerUtils.getInstance(getActivity());
        if (hasPermission()) {
            initMap();
            initIncidents();
        } else {
            requestPermissionsApp();
        }
    }

    private void initIncidents() {
        mViewModel.getResponseIncident().observe(getViewLifecycleOwner(), this::notifyDataList);
    }

    /**
     * init list of incident and update when has change
     *
     * @param items List<IncidentParent>
     */
    private void notifyDataList(List<IncidentEntity> items) {
        mOsmOverlay.clearOverlay();
        if (items.isEmpty()) {
            mMapView.invalidate();
        } else {
            for (int i = 0; i <= items.size() - 1; i++) {
                IncidentEntity incident = items.get(i);
                try {
                    if (incident != null & incident.isHasLocation()) {
                        incident.setType(items.get(i).getLocation().getType());
                        incident.setLatitude(items.get(i).getLocation().getCoordinates().get(1));
                        incident.setLongitude(items.get(i).getLocation().getCoordinates().get(0));
                        incident.setAggregatedEventId(items.get(i).getAggregatedEvent().getId());
                        mViewModel.appDatabase().incidentDao().insertOrUpdate(items.get(i));
                    }
                    //todo handle multi type location on the next version
                    MapUtils.addMarkerToMapWithCurrentUser(this.getContext(),
                            mOsmOverlay, mMapView, items.get(i));
                } catch (Exception e) {
                    Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initMap() {
        mMapView = MapUtils.createMapView(this.getContext(), 2, 18, mBinding.mapContainer);
        Drawable defaultMarker = Utils.getIcon(this.getContext(), R.drawable.marker_default);
        mOsmOverlay = new IncidentOSMOverlay(defaultMarker, this.getContext(), mMapView);
        mMapView.setClickable(true);
        mOsmOverlay.setOnMakerTapListener(this);
        mMapView.addMapListener(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (LocationManagerUtils.sLocationService.checkLocationService(requireContext())) {
            mMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocationManagerUtils.sLocationService.checkLocationService(requireContext())) {
            mMapView.onResume();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        if (input == null || input.isEmpty()) {
            mViewModel.cancelCountdown();
            mViewModel.loadIncidentsCountDown(mMapView);
        }
        return false;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

    @Override
    public void onTap(int index) {
        Object object = mOsmOverlay.getItem(index).getData();
        if (object == null) {
            return;
        }
        if (object instanceof IncidentEntity) {
            IncidentEntity incident = (IncidentEntity) object;
            onClickToDetail(incident);
        }
    }

    public void onClickToDetail(IncidentEntity incident) {
        if (incident != null) {
            SharedPrefUtil.getInstance().setIncident(incident);
            IncidentDetailActivity.toActivity(requireActivity());
            requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void getCurrentLocation(UpdateLocation location) {
        double currentLat;
        double currentLon;
        try {
            //get current location
            currentLat = location.getLocation().getLatitude();
            currentLon = location.getLocation().getLongitude();
            UserEntity userEntity = mViewModel.appDatabase().userDao().getUserInfo();
            userEntity.setLongitude(currentLon);
            userEntity.setLatitude(currentLat);
            mViewModel.appDatabase().userDao().update(userEntity);
            GeoPoint geoPoint = new GeoPoint(currentLat, currentLon);

            mRunnable = () -> MapUtils.zoomToCurrentLocation(getBaseActivity(), mMapView, geoPoint);
            mViewModel.getHandler().post(mRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void refreshIncident(RefreshIncident refresh) {
        mViewModel.cancelCountdown();
        mViewModel.loadIncidentsCountDown(mMapView);
    }

    @Subscribe()
    public void searchIncident(SearchInput input) {
        if (!input.getTextInput().isEmpty()) {
            this.input = input.getTextInput();
            notifyDataList(mViewModel.getIncidentsByTitle(input.getTextInput()));
        } else {
            this.input = "";
            initIncidents();
        }
    }

    public void createIncident() {
        IncidentCreateActivity.toActivity(this.getContext());
        requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRunnable != null) {
            mViewModel.getHandler().removeCallbacks(mRunnable);
        }
    }
}