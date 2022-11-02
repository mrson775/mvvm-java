package com.dallmeier.evidencer.ui.incidents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.utils.recycleview.BaseAdapter;
import com.dallmeier.evidencer.databinding.StateItemBinding;
import com.dallmeier.evidencer.model.incident_response.StateEntity;

import java.util.List;

public class StateIncidentAdapter extends BaseAdapter<StateEntity> {
    private final Context context;
    private LayoutInflater layoutInflater;

    public StateIncidentAdapter(Context context, List<StateEntity> items) {
        super(context, items);
        this.context = context;
    }

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        private final StateItemBinding binding;

        public StateViewHolder(final StateItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        StateItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.state_item, parent, false);
        return new StateViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, StateEntity val, int position) {
        StateViewHolder holderStatusViewHolder = (StateViewHolder) holder;
        holderStatusViewHolder.binding.setState(val);
        if (val.isTmpCheck()) {
            selectedPos = position;
        }
    }
}