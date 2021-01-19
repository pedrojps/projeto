package com.example.myapplication.common;

import android.view.View;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public abstract class FlexibleItemViewHolder<T> extends FlexibleViewHolder {

    public FlexibleItemViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
    }

    public abstract void bind(T item);

}
