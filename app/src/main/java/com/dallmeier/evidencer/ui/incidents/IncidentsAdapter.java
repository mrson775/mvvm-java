package com.dallmeier.evidencer.ui.incidents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.utils.recycleview.BaseAdapter;
import com.dallmeier.evidencer.databinding.IncidentItemBinding;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;
import com.dallmeier.evidencer.utils.recycleview.IRecycleComponent;

import java.util.List;

public class IncidentsAdapter extends BaseAdapter<IncidentEntity> {
    private Context context;
    private LayoutInflater layoutInflater;
    private IncidentsViewModel viewModel;
    private IRecycleComponent iRecycleComponent;

    public IncidentsAdapter(IncidentsViewModel viewModel, Context context, List<IncidentEntity> items, IRecycleCallback iRecycleCallback) {
        super(context, items, iRecycleCallback);
        this.context = context;
        selectedPos = 0;
        iRecycleComponent = this;
        this.viewModel=viewModel;
    }

    public static class IncidentViewHolder extends RecyclerView.ViewHolder {

        private final IncidentItemBinding binding;

        public IncidentViewHolder(final IncidentItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        IncidentItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.incident_item, parent, false);
        return new IncidentViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, IncidentEntity val, int position) {
        IncidentViewHolder holderIncidentViewHolder = (IncidentViewHolder) holder;
        holderIncidentViewHolder.binding.setIncident(val);
        holderIncidentViewHolder.binding.setIViewModel(viewModel);
        holderIncidentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClick(v, val, position);
            }
        });
    }

    public IRecycleComponent iRecycleComponent() {
        return iRecycleComponent;
    }
}