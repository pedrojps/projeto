package com.example.myapplication.ui.variaeisCategoriy;

import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class VarCategoriViewHolder extends FlexibleItemViewHolder<ItemCategoria> {

    private final ItemVariavelCBinding mBinding;

    public VarCategoriViewHolder(ItemVariavelCBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(ItemCategoria item) {
        mBinding.setVar(item);
    }
}
