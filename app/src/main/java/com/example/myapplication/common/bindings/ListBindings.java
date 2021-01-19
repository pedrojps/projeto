package com.example.myapplication.common.bindings;

import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import com.example.myapplication.common.adapter.MutableAdapter;

import java.util.List;

@SuppressWarnings("unchecked")
public class ListBindings {

    @BindingAdapter("app:items")
    public static <T> void setItems(ListView listView, List<T> items){
        if(listView.getAdapter() instanceof MutableAdapter){
            MutableAdapter<T> adapter = (MutableAdapter<T>) listView.getAdapter();

            if(adapter != null){
                adapter.updateDataSet(items);
            }
        }
    }

    @BindingAdapter("app:items")
    public static <T> void setItems(ExpandableListView view, List<T> items){
        if(view.getExpandableListAdapter() instanceof MutableAdapter){
            MutableAdapter<T> adapter = (MutableAdapter<T>) view.getExpandableListAdapter();

            if(adapter != null){
                adapter.updateDataSet(items);
            }
        }
    }

    @BindingAdapter("app:items")
    public static <T> void setItems(Spinner spinner, List<T> items){
        if(spinner.getAdapter() instanceof MutableAdapter){
            MutableAdapter<T> adapter = (MutableAdapter<T>) spinner.getAdapter();

            if(adapter != null){
                adapter.updateDataSet(items);
            }
        }
    }
}
