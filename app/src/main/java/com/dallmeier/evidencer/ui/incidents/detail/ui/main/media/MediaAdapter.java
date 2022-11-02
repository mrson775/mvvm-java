package com.dallmeier.evidencer.ui.incidents.detail.ui.main.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.utils.recycleview.BaseAdapter;
import com.dallmeier.evidencer.common.Statics;
import com.dallmeier.evidencer.databinding.MediaItemBinding;
import com.dallmeier.evidencer.model.evident.MediaAttachment;
import com.dallmeier.evidencer.utils.recycleview.IRecycleCallback;
import com.dallmeier.evidencer.utils.recycleview.IRecycleComponent;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaAdapter extends BaseAdapter<MediaAttachment> {
    private LayoutInflater layoutInflater;
    private Context context;
    Picasso picasso;
    private IRecycleComponent iRecycleComponent;

    public MediaAdapter(Context context, List<MediaAttachment> items, Picasso picasso, IRecycleCallback iRecycleCallback) {
        super(context, items, iRecycleCallback);
        this.picasso = picasso;
        this.context = context;
        iRecycleComponent = this;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final MediaItemBinding binding;

        public MyViewHolder(final MediaItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    @Override
    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        MediaItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.media_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, MediaAttachment val, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        switch ((val.getMimeType())) {
            case Statics.MIMETYPE_VIDEO:
                picasso.load(R.drawable.ic_play_white_200).placeholder(R.drawable.progress_animation).error(R.drawable.ic_no_img).into(myViewHolder.binding.thumbImg);
                break;
            case Statics.MIME_TYPE_AUDIO:
            case Statics.MIME_TYPE_AUDIO_MPEG:
                picasso.load(R.drawable.ic_audio_white_100).placeholder(R.drawable.progress_animation).error(R.drawable.ic_no_img).into(myViewHolder.binding.thumbImg);
                break;
            case Statics.MIMETYPE_IMAGE:
            case Statics.MIMETYPE_IMAGE_PNG:
                picasso.load(val.getUrl().trim()).placeholder(R.drawable.progress_animation).error(R.drawable.ic_no_img).into(myViewHolder.binding.thumbImg);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
