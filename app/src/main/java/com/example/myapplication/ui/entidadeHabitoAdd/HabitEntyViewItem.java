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

    private final HabitEnty mProjeto;

    public HabitEntyViewItem(HabitEnty obra) {
        this.mProjeto = obra;
    }

    public HabitEnty getModel() {
        return mProjeto;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return mProjeto.equals(o);
    }

    @Override
    public int hashCode() {
        return mProjeto.hashCode();
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
        holder.bind(mProjeto);
    }
}
