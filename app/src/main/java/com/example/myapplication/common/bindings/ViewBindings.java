package com.example.myapplication.common.bindings;

import android.view.View;

import androidx.databinding.BindingAdapter;

public class ViewBindings {

    @BindingAdapter("app:showView")
    public static void toggleVisibility(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
