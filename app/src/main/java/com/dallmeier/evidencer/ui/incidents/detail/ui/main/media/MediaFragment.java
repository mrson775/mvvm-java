package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media;

import static com.dallmeier.evidencer.common.Statics.ARG_SECTION_NUMBER_TIMELINE;
import static com.dallmeier.evidencer.common.Statics.ID_EVIDENT;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseFragment;
import com.dallmeier.evidencer.databinding.FragmentMediaBinding;
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

@AndroidEntryPoint
public class MediaFragment extends BaseFragment<FragmentMediaBinding, MediaViewModel> implements SwipeRefreshLayout.OnRefreshListener, RecycleClickListener, IRecycleCallback {
    private FragmentMediaBinding mBinding;
    private MediaAdapter mAdapter;
    private Runnable mRunnable;
    private IncidentEntity incident;
    private final int size = 10;
    private final String sort = "dateObserved,desc";
    private AggregatedEventId aggregatedEventId;

    public static MediaFragment newInstance() {
        return new MediaFragment();
    }

    public static MediaFragment newInstance(int index, long incidentId) {
        MediaFragment fragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER_TIMELINE, index);
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
        return R.layout.fragment_media;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        showLoading();
        initData();
        initViews();
    }

    private void initViews() {
        mViewModel.getMediaListLiveData().observe(getViewLifecycleOwner(), this::notifyDataList);
        mBinding.swipeRefreshLayoutTimelineFragment.setOnRefreshListener(this);
        initRequest();
    }

    private void initData() {
        incident = SharedPrefUtil.getInstance().getIncident();
        aggregatedEventId = new AggregatedEventId(incident.getLastAggregatedId());
    }

    private void initRequest() {
        mViewModel.getMediaAttachments(aggregatedEventId, 0, size, sort);
    }

    /**
     * live data Evident list
     *
     * @param evidentEntities List<EvidentEntity>
     */
    private void notifyDataList(List<MediaAttachment> evidentEntities) {
        if (mAdapter == null) {
            mAdapter = new MediaAdapter(getContext(), evidentEntities, mViewModel.picasso(), this);
            mAdapter.setItemClickListener(this);
            mBinding.timelineRcv.setAdapter(mAdapter);
            mAdapter.iRecycleComponent().loadMore(mBinding.timelineRcv);
        } else {
            mAdapter.addItems(evidentEntities);
        }
        isVisibleRecord(mAdapter.getList());
        hideLoading();
    }

    private void isVisibleRecord(List<MediaAttachment> listMedia) {
        if (listMedia.size() == 0) {
            mBinding.swipeRefreshLayoutTimelineFragment.setVisibility(View.GONE);
            mBinding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
        } else {
            mBinding.swipeRefreshLayoutTimelineFragment.setVisibility(View.VISIBLE);
            mBinding.emptyLayout.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.clearData();
        }
        initRequest();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mBinding.swipeRefreshLayoutTimelineFragment.setRefreshing(false);
            }
        };
        mViewModel.getHandler().postDelayed(mRunnable, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mViewModel.getHandler() != null && mRunnable != null) {
            mViewModel.getHandler().removeCallbacks(mRunnable);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshTimeline(RefreshTimeline refreshTimeline) {
        requireActivity().runOnUiThread(() -> {
            if (mAdapter != null) {
                mAdapter.clearData();
            }
            initRequest();
        });
    }

    @Override
    public void onClick(View view, Object item, int position) {
        MediaAttachment mediaAttachment = (MediaAttachment) item;
        mViewModel.audioRecorder().play(mediaAttachment, getActivity());
    }

    /**
     * Recycle callback
     *
     * @param page
     */
    @Override
    public void callback(int page) {
        mViewModel.getMediaAttachments(aggregatedEventId, page, size, sort);
    }
}