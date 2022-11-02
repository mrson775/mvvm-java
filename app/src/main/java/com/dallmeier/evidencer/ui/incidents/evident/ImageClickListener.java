package com.dallmeier.evidencer.ui.incidents.evident;

import android.view.View;

import com.dallmeier.evidencer.model.ImageEntity;

import java.util.List;

public interface ImageClickListener {
    void onClick(View view, int pos, List<ImageEntity> postList);
    void onCheckBox(int pos, List<ImageEntity> postList);
    void onUnCheckBox(int pos, List<ImageEntity> postList);
}
