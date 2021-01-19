package com.example.myapplication.common.adapter;

import android.widget.Spinner;

public interface MutableSpinnerAdapter<T> extends MutableAdapter<T>{

    void setItemSelected(Spinner spinner, T item);
}
