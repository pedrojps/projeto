package com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;


public class VarCategoriCriateSpinnerViewHolder implements SpinnerAdapter.ISpinnerViewHolder<ItemCategoria>{

    private final ItemVariavelCriateBinding mBinding;

    public VarCategoriCriateSpinnerViewHolder(@NonNull ItemVariavelCriateBinding binding) {
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
