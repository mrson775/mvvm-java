package com.dallmeier.evidencer.listener;

import android.view.View;


public interface RecycleClickListener<T> {
    void onClick(View view, T item, int position);
}
