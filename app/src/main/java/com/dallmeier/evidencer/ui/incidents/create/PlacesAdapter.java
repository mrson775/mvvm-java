package com.dallmeier.evidencer.ui.incidents.create;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.databinding.PlaceItemBinding;
import com.dallmeier.evidencer.model.place.Places;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {
    private final List<Places> postList;
    private LayoutInflater layoutInflater;
    private final Context context;
    private PlacesClickListener clickListener;

    public PlacesAdapter(Context context) {
        this.postList = new ArrayList<>();
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final PlaceItemBinding binding;

        public MyViewHolder(final PlaceItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        PlaceItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.place_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.binding.setPlace(postList.get(position));
        holder.itemView.setOnClickListener(v -> setOnClick(v, position, postList));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updateReceiptsList(List<Places> postListNew) {
        postList.clear();
        postList.addAll(postListNew);
        Log.d("sssList", "postList " + postList.size());
    }

    private void setOnClick(View view, int pos, List<Places> postList) {
        clickListener.onClick(view, pos, postList);
    }

    public void setItemClickListener(PlacesClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public List<Places> getPostList() {
        return postList;
    }
}
