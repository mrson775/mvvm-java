package com.dallmeier.evidencer.ui.incidents.create;

import android.view.View;

import com.dallmeier.evidencer.model.place.Places;

import java.util.List;

public interface PlacesClickListener {
    void onClick(View view, int pos, List<Places> postList);
}
