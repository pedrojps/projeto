package com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro;

import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class VarCategoriCriateViewHolder extends FlexibleItemViewHolder<ItemCategoria> {

    private final ItemVariavelCriateBinding mBinding;

    public VarCategoriCriateViewHolder(ItemVariavelCriateBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(ItemCategoria item) {
        mBinding.setVar(item);
    }
}
