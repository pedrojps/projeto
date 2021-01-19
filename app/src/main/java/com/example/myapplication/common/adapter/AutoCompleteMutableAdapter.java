package com.example.myapplication.common.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteMutableAdapter
        extends ArrayAdapter<String>
        implements MutableAdapter<String> {

    private List<String> mSource = new ArrayList<>();

    public AutoCompleteMutableAdapter(@NonNull Context context, @NonNull List<String> items) {
        super(context, android.R.layout.simple_list_item_1);
        mSource = items;
    }

    @Override
    public void updateDataSet(@NonNull List<String> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    private void setItems(@NonNull List<String> items) {
        mSource.clear();
        mSource.addAll(items);
    }

    @Override
    public int getCount() {
        return mSource.size();
    }

    @Override
    public String getItem(int i) {
        return mSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEmpty() {
        return mSource.isEmpty();
    }

}
