package com.dallmeier.evidencer.ui.incidents.evident;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.databinding.ImageItemBinding;
import com.dallmeier.evidencer.model.ImageEntity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private final List<ImageEntity> postList;
    private final List<ImageEntity> postListSelected;
    private LayoutInflater layoutInflater;
    private final Context context;
    private ImageClickListener clickListener;

    public ImageAdapter(Context context) {
        this.postList = new ArrayList<>();
        postListSelected = new ArrayList<>();
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageItemBinding binding;

        public MyViewHolder(final ImageItemBinding itemBinding) {
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
        ImageItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.image_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(postList.get(position).getImage()));
        Picasso.get().load(uri).resize(100, 100).placeholder(R.drawable.ic_empty_image).into(holder.binding.image);
        holder.binding.imageCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    postList.get(position).setSelected(true);
                    postListSelected.add(postList.get(position));
                } else {
                    postList.get(position).setSelected(false);
                    postListSelected.remove(postList.get(position));
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void updateReceiptsList(List<ImageEntity> postListNew) {
        postList.clear();
        postList.addAll(postListNew);
        Log.d("sssList", "postList " + postList.size());
    }

    private void setOnClick(View view, int pos, List<ImageEntity> postList) {
        clickListener.onClick(view, pos, postList);
    }

    private void setCheckBoxSelected(int pos, List<ImageEntity> postList) {
        clickListener.onCheckBox(pos, postList);
    }

    private void setUnCheckBoxSelected(int pos, List<ImageEntity> postList) {
        clickListener.onCheckBox(pos, postList);
    }

    public void setItemClickListener(ImageClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public List<ImageEntity> getPostList() {
        return postList;
    }

    public List<ImageEntity> getPostListSelected() {
        return postListSelected;
    }
}
