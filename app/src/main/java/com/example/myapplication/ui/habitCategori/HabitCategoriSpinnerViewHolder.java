package com.example.myapplication.ui.habitCategori;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;


public class HabitCategoriSpinnerViewHolder implements SpinnerAdapter.ISpinnerViewHolder<HabitCategoria>{

    private final ItemHabitCBinding mBinding;

    public HabitCategoriSpinnerViewHolder(@NonNull ItemHabitCBinding binding) {
        this.mBinding = binding;
    }

    @Override
    public void bind(HabitCategoria data) {
        mBinding.setHabit(data);
    }

    @Override
    public View getView() {
        return mBinding.getRoot();
    }
}
