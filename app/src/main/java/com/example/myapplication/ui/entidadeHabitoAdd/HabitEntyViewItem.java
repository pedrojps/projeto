package com.example.myapplication.ui.entidadeHabitoAdd;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.databinding.ItemHabitEBinding;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class HabitEntyViewItem extends AbstractFlexibleItem<HabitEntyViewHolder> {

    private final HabitEnty mHabitEnty;

    public HabitEntyViewItem(HabitEnty obra) {
        this.mHabitEnty = obra;
    }

    public HabitEnty getModel() {
        return mHabitEnty;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return mHabitEnty.equals(o);
    }

    @Override
    public int hashCode() {
        return mHabitEnty.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_habit_e;
    }

    @Override
    public HabitEntyViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemHabitEBinding binding = DataBindingUtil.bind(view);
        return new HabitEntyViewHolder(binding, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HabitEntyViewHolder holder, int position, List payloads) {
        holder.bind(mHabitEnty);
    }
}
