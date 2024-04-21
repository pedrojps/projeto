package com.example.myapplication.ui.entidadeHabitoAdd;

import android.graphics.Bitmap;

import com.example.myapplication.R;
import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitEBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;
import com.example.myapplication.utils.ImageUtil;

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
        ImageUtil.INSTANCE.readSetImage(itemView.getContext(),item.getCategoriaId()+"",mBinding.imageIcon);
    }
}
