package com.example.myapplication.ui.habitCategori;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class HabitCategoriViewItem extends AbstractFlexibleItem<HabitCategoriViewHolder> {

    private final HabitCategoria mProjeto;

    public HabitCategoriViewItem(HabitCategoria obra) {
        this.mProjeto = obra;
    }

    public HabitCategoria getModel() {
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
        return R.layout.item_habit_c;
    }

    @Override
    public HabitCategoriViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemHabitCBinding binding = DataBindingUtil.bind(view);
        return new HabitCategoriViewHolder(binding, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HabitCategoriViewHolder holder, int position, List payloads) {
        holder.bind(mProjeto);
    }
}
