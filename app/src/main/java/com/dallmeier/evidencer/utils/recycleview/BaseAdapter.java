package com.dallmeier.evidencer.utils.recycleview;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.listener.RecycleClickListener;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IRecycleComponent {
    private Context context;
    private List<T> items;
    private RecycleClickListener clickListener;
    protected int selectedPos = RecyclerView.NO_POSITION;
    private IRecycleCallback iRecycleCallback;
    private int page = 0;

    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);

    public BaseAdapter(Context context, List<T> items, IRecycleCallback iRecycleCallback) {
        this.context = context;
        this.items = items;
        this.iRecycleCallback = iRecycleCallback;
    }

    public BaseAdapter(Context context, List<T> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindData(holder, items.get(position), position);
        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<T> postListNew) {
        items.addAll(postListNew);
        this.notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public List<T> getList() {
        return items;
    }

    public void setOnClick(View view, T item, int position) {
        notifyItemChanged(selectedPos);
        selectedPos = position;
        notifyItemChanged(selectedPos);
        clickListener.onClick(view, item, position);
    }

    public void setItemClickListener(RecycleClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void clearData() {
        items.clear();
        //Collections.reverse(this.postList);
        Log.d("items", "items " + items.size());
    }

    @Override
    public void loadMore(RecyclerView recyclerView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1)
                            && dy > 0) {
                        try {
                            page++;
                            iRecycleCallback.callback(page);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}