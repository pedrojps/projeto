package com.example.myapplication.common.bindings;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.databinding.BindingAdapter;

import java.util.List;

public class AutoCompleteStringListBindings {

    @BindingAdapter("app:items")
    public static void setItems(AutoCompleteTextView view, List<String> items){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(), android.R.layout.simple_list_item_1, items
        );

        view.setAdapter(adapter);
        view.setThreshold(1);
    }
}
