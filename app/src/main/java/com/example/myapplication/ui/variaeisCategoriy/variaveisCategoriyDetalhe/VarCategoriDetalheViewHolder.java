package com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe;

import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;
import com.example.myapplication.databinding.ItemVariavelDetalheBinding;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class VarCategoriDetalheViewHolder extends FlexibleItemViewHolder<ItemCategoria> {

    private final ItemVariavelDetalheBinding mBinding;

    public VarCategoriDetalheViewHolder(ItemVariavelDetalheBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(ItemCategoria item) {
        mBinding.setVar(item);
    }
}
