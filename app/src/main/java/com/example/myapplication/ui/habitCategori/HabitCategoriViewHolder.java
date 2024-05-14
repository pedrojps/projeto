package com.example.myapplication.ui.habitCategori;

import android.graphics.Bitmap;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.common.FlexibleItemViewHolder;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.source.local.projection.HabitAlertProject;
import com.example.myapplication.databinding.ItemHabitCBinding;
import com.example.myapplication.utils.ImageUtil;

import java.util.Objects;

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
        if (item.getDiscricao() == null || Objects.equals(item.getDiscricao(), ""))
            mBinding.discrition.setVisibility(View.GONE);
        if (item.getClass() == HabitAlertProject.class){
            mBinding.time.setText(itemView.getContext().getString(R.string.format_time_item_alert, ((HabitAlertProject) item).getTime()));
        }
        else
            mBinding.time.setText(itemView.getContext().getString(R.string.format_date_item, item.getDataCriat()));
    }
}
