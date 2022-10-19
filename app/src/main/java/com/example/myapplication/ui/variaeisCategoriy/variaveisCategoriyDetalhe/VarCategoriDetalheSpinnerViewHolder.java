package com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;
import com.example.myapplication.databinding.ItemVariavelDetalheBinding;


public class VarCategoriDetalheSpinnerViewHolder implements SpinnerAdapter.ISpinnerViewHolder<ItemCategoria>{

    private final ItemVariavelDetalheBinding mBinding;

    public VarCategoriDetalheSpinnerViewHolder(@NonNull ItemVariavelDetalheBinding binding) {
        this.mBinding = binding;
    }

    @Override
    public void bind(ItemCategoria data) {
        mBinding.setVar(data);
    }

    @Override
    public View getView() {
        return mBinding.getRoot();
    }
}
