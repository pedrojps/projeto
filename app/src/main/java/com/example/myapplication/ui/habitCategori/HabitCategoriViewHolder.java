package com.example.myapplication.ui.habitCategori;

import android.graphics.Bitmap;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;
import com.example.myapplication.utils.ImageUtil;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class HabitCategoriViewHolder extends FlexibleItemViewHolder<HabitCategoria> {

    private final ItemHabitCBinding mBinding;

    public HabitCategoria mItem;

    public HabitCategoriViewHolder(ItemHabitCBinding binding, FlexibleAdapter adapter) {
        super(binding.getRoot(), adapter);
        mBinding = binding;
    }

    @Override
    public void bind(HabitCategoria item) {
        mItem = item;
        mBinding.setHabit(item);
        ImageUtil.INSTANCE.readSetImage(itemView.getContext(),item.getId()+"", mBinding.imageIcon);
    }
}
