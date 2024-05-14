package com.example.myapplication.ui.main;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.data.notification.NotificationUtils;
import com.example.myapplication.data.source.local.projection.HabitAlertProject;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewHolder;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

public class HabitAdapterHome<H extends AbstractFlexibleItem<HabitCategoriViewHolder>> extends FlexibleAdapter {

    List<HabitCategoriViewItem> mItens = null;

    public HabitAdapterHome(@Nullable List items) {
        super(items);
    }

    public HabitAdapterHome(@Nullable List items, @Nullable Object listeners) {
        super(items, listeners);
    }

    public HabitAdapterHome(@Nullable List items, @Nullable Object listeners, boolean stableIds) {
        super(items, listeners, stableIds);
    }

    public void updateDataSet(@Nullable List items, boolean animate) {
        super.updateDataSet(items, animate);
    }

    public void updateDataSet(@Nullable List items, boolean animate, boolean load) {
        items = NotificationUtils.INSTANCE.sortItens(items);
        super.updateDataSet(items, animate);
        if (load) mItens = items;
    }

    @Nullable
    @Override
    public HabitCategoriViewItem getItem(int position) {
        return (HabitCategoriViewItem) super.getItem(position);
    }

    public void filter(String text){
        List<HabitCategoriViewItem> list = new ArrayList<>();

        if (text == null) return;

        try {
            for (HabitCategoriViewItem h: mItens){
                String name = h.getModel().getNome();
                String discricao = h.getModel().getDiscricao();
                if (name==null)
                    name = "";
                if (discricao==null)
                    discricao = "";
                if (name.toLowerCase().contains(text.toLowerCase())
                        || discricao.toLowerCase().contains(text.toLowerCase())){
                    list.add(h);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list.addAll(mItens);
        }


        updateDataSet(list,true,false);
    }
}
