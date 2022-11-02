package com.dallmeier.evidencer.ui.incidents;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseFragment;
import com.dallmeier.evidencer.databinding.FragmentIncidentsBinding;
import com.dallmeier.evidencer.listener.EventBus.SearchInput;
import com.dallmeier.evidencer.listener.RecycleClickListener;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.ui.incidents.detail.IncidentDetailActivity;
import com.dallmeier.evidencer.utils.EvidentOSMOverlay;
import com.dallmeier.evidencer.utils.IncidentOSMOverlay;
import com.dallmeier.evidencer.utils.MapUtils;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.Utils;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;
import org.osmdroid.views.MapView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IncidentsFragment extends BaseFragment<FragmentIncidentsBinding, IncidentsViewModel> implements SwipeRefreshLayout.OnRefreshListener, EvidentOSMOverlay.OnMarkerTapListener, RecycleClickListener, IRecycleCallback {
    public static IncidentsFragment newInstance() {
        return new IncidentsFragment();
    }

    private FragmentIncidentsBinding mBinding;
    private IncidentsAdapter mAdapter;
    private StateIncidentAdapter mStateAdapter;
    private final int size = 10;
    private final String sort = "id,desc";
    private MapView mMapView;
    private IncidentOSMOverlay mOsmOverlay;
    private Runnable mRunnable;
    private final Handler mHandler = new Handler();

    @Override
    public int getBindingVariable() {
        return BR.viewModelCallback;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_incidents;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        mBinding.setFragmentCallback(this);
        mRunnable = () -> requireActivity().runOnUiThread(() -> {
            initIncidents();
            observeIncidents();
            initRecycleView();
            initMapView();
        });
        mHandler.postDelayed(mRunnable, 150);
    }

    private void initStateAdapter(IncidentEntity incident) {
        mViewModel.resetState();
        mViewModel.setState(incident);
        mStateAdapter = new StateIncidentAdapter(this.getContext(), mViewModel.appDatabase().stateDao().getStates());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.incidentDetailView.rcvState.setLayoutManager(layoutManager);
        mBinding.incidentDetailView.rcvState.setAdapter(mStateAdapter);
    }

    private void initMapView() {
        mMapView = MapUtils.createMapView(this.getContext(), 2, 18, mBinding.mapContainer);
        Drawable defaultMarker = Utils.getIcon(this.getContext(), R.drawable.marker_default);
        mOsmOverlay = new IncidentOSMOverlay(defaultMarker, this.getContext(), mMapView);
        mOsmOverlay.setOnMakerTapListener(this);
    }

    private void initRecycleView() {
        mBinding.swipeRefresh.setOnRefreshListener(this);
    }

    private void initIncidents() {
        mViewModel.getIncidents(String.valueOf(0), size, sort);
    }

    private void observeIncidents() {
        mViewModel.getResponseIncident().observe(this, this::notifyDataList);
    }

    /**
     * init list of incident and update when has change
     *
     * @param items List<IncidentParent>
     */
    private void notifyDataList(List<IncidentEntity> items) {
        isVisibleRecord(items);
        if (mAdapter == null && items.size() > 0) {
            mAdapter = new IncidentsAdapter(mViewModel, this.getContext(), items, this);
            mAdapter.setItemClickListener(this);
            mBinding.incidentsRcv.setAdapter(mAdapter);
            mAdapter.addItems(items);
            if (items.size() > 0) {
                viewDetail(items.get(0));
                mAdapter.notifyItemChanged(0);
            }
            mAdapter.iRecycleComponent().loadMore(mBinding.incidentsRcv);
        } else {
            assert mAdapter != null;
            mAdapter.addItems(items);
        }
    }

    private void isVisibleRecord(List<IncidentEntity> incidents) {
        if (incidents.isEmpty()) {
            mBinding.containerLn.setVisibility(View.GONE);
            mBinding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
        } else {
            mBinding.containerLn.setVisibility(View.VISIBLE);
            mBinding.emptyLayout.getRoot().setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void viewDetail(IncidentEntity incident) {
        try {
            SharedPrefUtil.getInstance().setIncident(incident);
            updateReadFlag(incident);
            String incidentNbo = "<b>" + incident.getIncidentNbo() + "</b><br> " + Utils.getFormattedDate(incident.getTimeStampCreated());
            String type = "";
            try {
                type = "<b>" + getString(R.string.type) + "</b>: " + mViewModel.appDatabase().incidentTypeDao().searchById(incident.getIncidentTypeId()).get(0).getDescription();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String assignee = "";
            try {
                assignee = "<b>" + getString(R.string.assignee) + "</b>: " + mViewModel.appDatabase().assigneeDao().searchById(incident.getAssigneeId()).getUsername();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String location = "<b>" + getString(R.string.location) + "</b>: " + incident.getAddress();
            String pieces = "<b>" + getString(R.string.pieces) + "</b> : " + incident.getNumberAllEvidences();
            mBinding.incidentDetailView.tvNameTime.setText(Html.fromHtml(incidentNbo));
            mBinding.incidentDetailView.tvTypeIncident.setText(Html.fromHtml(type));
            mBinding.incidentDetailView.tvPieces.setText(Html.fromHtml(pieces));
            mBinding.incidentDetailView.tvAssignee.setText(Html.fromHtml(assignee));
            mBinding.incidentDetailView.tvLocation.setText(Html.fromHtml(location));
            if (incident.getThumbnailURL() != null) {
                mViewModel.picasso().load(incident.getThumbnailURL()).placeholder(R.drawable.progress_animation).error(R.drawable.ic_no_img).into(mBinding.incidentDetailView.incidentThumb);
            }
            initStateAdapter(incident);
            mOsmOverlay.clearOverlay();
            MapUtils.addMarkerSingleWithAllUser(this.getContext(),
                    mOsmOverlay, mMapView, incident);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param incident Incident of click Event
     */
    private void updateReadFlag(IncidentEntity incident) {
        IncidentEntity incidentCurrent = mViewModel.appDatabase().incidentDao().getIncidentById(incident.getId());
        if (incidentCurrent != null) {
            incidentCurrent.setRead(true);
            mViewModel.updateIncident(incidentCurrent);
        }
    }

    public void onRefresh() {
        mAdapter.clearData();
        mViewModel.getIncidents(String.valueOf(0), size, sort);
        new Handler().postDelayed(() -> mBinding.swipeRefresh.setRefreshing(false), 1000);

    }

    @Override
    public void onStop() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onStop();
    }

    @Override
    public void onTap(int index) {
        onClickToDetail();
    }

    public void onClickToDetail() {
        IncidentDetailActivity.toActivity(this.getContext());
        requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Subscribe()
    public void searchIncident(SearchInput input) {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
        if (!input.getTextInput().isEmpty()) {
            notifyDataList(mViewModel.getIncidentsByTitle(input.getTextInput()));
        } else {
            initIncidents();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onClick(View view, Object item, int position) {
        //todo handle multi type location on the next version
        IncidentEntity incident = (IncidentEntity) item;
        if (incident.getLastLatitude() == 0)
            return;
        viewDetail(incident);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void callback(int page) {
        mViewModel.getIncidents(String.valueOf(page), size, sort);
    }
}
