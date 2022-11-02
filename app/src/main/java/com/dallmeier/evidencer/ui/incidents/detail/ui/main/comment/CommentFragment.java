package com.dallmeier.evidencer.ui.incidents.detail.ui.main.comment;

import static android.view.View.VISIBLE;
import static com.dallmeier.evidencer.common.Statics.ARG_SECTION_NUMBER_COMMENT;
import static com.dallmeier.evidencer.common.Statics.ID_EVIDENT;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dallmeier.evidencer.BR;
import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseFragment;
import com.dallmeier.evidencer.databinding.FragmentCommentBinding;
import com.dallmeier.evidencer.listener.EventBus.RefreshComment;
import com.dallmeier.evidencer.model.Comment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentFragment extends BaseFragment<FragmentCommentBinding, CommentViewModel> implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentCommentBinding mBinding;
    private long incidentId;
    private CommentAdapter mAdapter;
    private Runnable mRunnable;
    private Handler mHandler = new Handler();

    public static CommentFragment newInstance(long index, long incidentId) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_SECTION_NUMBER_COMMENT, index);
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
        return R.layout.fragment_comment;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = getViewDataBinding();
        initViews();
    }


    private void initViews() {
        mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    private void isVisibleRecord(List<Comment> events) {
        if (events.size() == 0) {
            mBinding.swipeRefreshLayout.setVisibility(View.GONE);
            mBinding.emptyLayout.getRoot().setVisibility(VISIBLE);
        } else {
            mBinding.swipeRefreshLayout.setVisibility(VISIBLE);
            mBinding.emptyLayout.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        initData();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mBinding.swipeRefreshLayout.setRefreshing(false);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);
    }

    private void initData() {
        if (getArguments() != null) {
            incidentId = getArguments().getLong(ID_EVIDENT);
        }
        mViewModel.setCommentsLiveData(incidentId);
        mViewModel.getCommentsLive().observe(getViewLifecycleOwner(), this::notifyDataList);
    }

    private void notifyDataList(List<Comment> commentEntities) {
        isVisibleRecord(commentEntities);
        if (commentEntities.size() == 0)
            return;
        if (mAdapter == null) {
            mAdapter = new CommentAdapter(getContext(), commentEntities);
            mBinding.commentRcv.setAdapter(mAdapter);
        } else {
            mAdapter.clearData();
            mAdapter.addItems(commentEntities);
        }
        mViewModel.updateIncident(incidentId);
    }

    @Subscribe
    public void refreshComment(RefreshComment refreshComment) {
        mViewModel.setCommentsLiveData(incidentId);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}