package com.dallmeier.evidencer.ui.incidents.detail.ui.main.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.utils.recycleview.BaseAdapter;
import com.dallmeier.evidencer.databinding.EvidentItemBinding;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;
import com.dallmeier.evidencer.utils.recycleview.IRecycleComponent;

import java.util.List;

public class TimelineAdapter extends BaseAdapter<MediaAttachment> {
    private LayoutInflater layoutInflater;
    private final Context context;
    private IRecycleComponent iRecycleComponent;

    public TimelineAdapter(Context context, List<MediaAttachment> items, IRecycleCallback iRecycleCallback) {
        super(context, items, iRecycleCallback);
        this.context = context;
        iRecycleComponent = this;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final EvidentItemBinding binding;

        public MyViewHolder(final EvidentItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        EvidentItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.evident_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, MediaAttachment val, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.binding.setMediaAttachment(val);
        holder.itemView.setOnClickListener(v -> setOnClick(v, val, position));
    }

    public IRecycleComponent iRecycleComponent() {
        return iRecycleComponent;
    }
}
