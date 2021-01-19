package com.example.myapplication.ui.habitCategori;

import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class HabitCategoriViewHolder extends FlexibleItemViewHolder<HabitCategoria> {

    private final ItemHabitCBinding mBinding;

    public HabitCategoriViewHolder(ItemHabitCBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(HabitCategoria item) {
        mBinding.setHabit(item);
    }
}
