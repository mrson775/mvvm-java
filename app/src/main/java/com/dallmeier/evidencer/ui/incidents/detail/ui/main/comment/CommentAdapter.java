package com.dallmeier.evidencer.ui.incidents.detail.ui.main.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.utils.recycleview.BaseAdapter;
import com.dallmeier.evidencer.databinding.CommentItemBinding;
import com.dallmeier.evidencer.model.Comment;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentAdapter extends BaseAdapter<Comment> {
    private Context context;
    private LayoutInflater layoutInflater;

    public CommentAdapter(Context context, List<Comment> items, IRecycleCallback iRecycleCallback) {
        super(context, items, iRecycleCallback);
        this.context = context;
    }
    public CommentAdapter(Context context, List<Comment> items) {
        super(context, items);
        this.context = context;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CommentItemBinding binding;

        public MyViewHolder(final CommentItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        CommentItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.incident_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, Comment val, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.binding.setComment(val);
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        CommentItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.comment_item, parent, false);
        return new MyViewHolder(binding);
    }
}
