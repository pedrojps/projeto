package com.example.myapplication.common.bindings;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.databinding.BindingAdapter;

import com.example.myapplication.common.adapter.MutableSpinnerAdapter;


public class SpinnerBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:selected")
    public static <T> void setSelected(Spinner spinner, T itemSelected) {
        SpinnerAdapter adapter = spinner.getAdapter();

        if(adapter != null && adapter instanceof MutableSpinnerAdapter){
            MutableSpinnerAdapter<T> mutableAdapter = (MutableSpinnerAdapter<T>) spinner.getAdapter();
            mutableAdapter.setItemSelected(spinner, itemSelected);
        }
    }
}
