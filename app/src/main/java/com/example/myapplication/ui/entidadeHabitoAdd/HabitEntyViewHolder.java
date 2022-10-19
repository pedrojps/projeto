package com.example.myapplication.ui.entidadeHabitoAdd;

import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitEBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class HabitEntyViewHolder extends FlexibleItemViewHolder<HabitEnty> {

    private final ItemHabitEBinding mBinding;

    public HabitEntyViewHolder(ItemHabitEBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(HabitEnty item) {
        mBinding.setHabit(item);
    }
}
