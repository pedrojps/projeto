package com.example.myapplication.ui.entidadeHabitoAdd;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitEBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;


public class HabitEntySpinnerViewHolder implements SpinnerAdapter.ISpinnerViewHolder<HabitEnty>{

    private final ItemHabitEBinding mBinding;

    public HabitEntySpinnerViewHolder(@NonNull ItemHabitEBinding binding) {
        this.mBinding = binding;
    }

    @Override
    public void bind(HabitEnty data) {
        mBinding.setHabit(data);
    }

    @Override
    public View getView() {
        return mBinding.getRoot();
    }
}
