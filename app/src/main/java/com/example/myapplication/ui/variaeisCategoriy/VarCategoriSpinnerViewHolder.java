package com.example.myapplication.ui.variaeisCategoriy;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;


public class VarCategoriSpinnerViewHolder implements SpinnerAdapter.ISpinnerViewHolder<ItemCategoria>{

    private final ItemVariavelCBinding mBinding;

    public VarCategoriSpinnerViewHolder(@NonNull ItemVariavelCBinding binding) {
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
