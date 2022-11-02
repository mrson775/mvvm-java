package com.dallmeier.evidencer.ui.incidents.detail.ui.main.timeline;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseFragment;
import com.dallmeier.evidencer.databinding.FragmentTimelineBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshTimeline;
import com.dallmeier.evidencer.listener.RecycleClickListener;
import com.dallmeier.evidencer.model.AggregatedEventId;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.dallmeier.evidencer.common.Statics.ARG_SECTION_NUMBER_TIMELINE;
import static com.dallmeier.evidencer.common.Statics.ID_EVIDENT;

@AndroidEntryPoint
public class TimelineFragment extends BaseFragment<FragmentTimelineBinding, TimelineViewModel> implements RecycleClickListener, SwipeRefreshLayout.OnRefreshListener, IRecycleCallback {
    private FragmentTimelineBinding mBinding;
    private TimelineAdapter mAdapter;
    private IncidentEntity incident;
    private Runnable mRunnable;
    private int page;
    private final int size = 10;
    private final String sort = "dateObserved,desc";
    private AggregatedEventId aggregatedEventId;

    public static TimelineFragment newInstance(long index, long incidentId) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_SECTION_NUMBER_TIMELINE, index);
        bundle.putLong(ID_EVIDENT, incidentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModelCallback;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_timeline;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        initViews();
        initData();
    }

    private void initViews() {
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        incident = SharedPrefUtil.getInstance().getIncident();
        aggregatedEventId = new AggregatedEventId(incident.getLastAggregatedId());
        initRequest();
        observeTimeline();
    }

    private void initRequest() {
        page = 0;
        mViewModel.getMediaAttachments(aggregatedEventId, page, size, sort);
    }

    private void observeTimeline() {
        mViewModel.getMediaListLiveData().observe(getViewLifecycleOwner(), this::notifyDataList);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * live data Evident list
     *
     * @param evidentEntities List<EvidentEntity>
     */
    private void notifyDataList(List<MediaAttachment> evidentEntities) {
        if (mAdapter == null) {
            mAdapter = new TimelineAdapter(getContext(), evidentEntities, this);
            mAdapter.setItemClickListener(this);
            mBinding.timelineRcv.setAdapter(mAdapter);
            mAdapter.iRecycleComponent().loadMore(mBinding.timelineRcv);
        } else {
            mAdapter.addItems(evidentEntities);
        }
        isVisibleRecord(mAdapter.getList());
    }

    /**
     * fix duplicate:
     * upload success->back to the list of incident->resume timeline
     */
    private void resetList() {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
    }

    private void isVisibleRecord(List<MediaAttachment> events) {
        if (events.size() == 0) {
            mBinding.swipeRefreshLayout.setVisibility(View.GONE);
            mBinding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
        } else {
            mBinding.swipeRefreshLayout.setVisibility(View.VISIBLE);
            mBinding.emptyLayout.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        resetList();
        initRequest();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        };
        mViewModel.getHandler().postDelayed(mRunnable, 1000);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshTimeline(RefreshTimeline refreshTimeline) {
        requireActivity().runOnUiThread(() -> {
            resetList();
            initRequest();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mViewModel.getHandler() != null && mRunnable != null) {
            mViewModel.getHandler().removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onClick(View view, Object item, int position) {
        MediaAttachment mediaAttachment = (MediaAttachment) item;
        mViewModel.audioRecorder().play(mediaAttachment, getActivity());
    }

    @Override
    public void callback(int page) {
        mViewModel.getMediaAttachments(aggregatedEventId, page, size, sort);
    }
}