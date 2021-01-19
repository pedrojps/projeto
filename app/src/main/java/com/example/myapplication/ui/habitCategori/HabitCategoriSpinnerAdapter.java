package com.example.myapplication.ui.habitCategori;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;

import java.util.List;


public class HabitCategoriSpinnerAdapter extends SpinnerAdapter<HabitCategoria, HabitCategoriSpinnerViewHolder> {

    public HabitCategoriSpinnerAdapter(@NonNull List<HabitCategoria> data) {
        super(data);
    }

    @Override
    public HabitCategoriSpinnerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemHabitCBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_habit_c, parent, false);
        return new HabitCategoriSpinnerViewHolder(binding);
    }

    @Override
    public void bindViewHolder(HabitCategoriSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public HabitCategoriSpinnerViewHolder createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemHabitCBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_habit_c, parent, false);
        return new HabitCategoriSpinnerViewHolder(binding);
    }

    @Override
    public void bindDropDownViewHolder(HabitCategoriSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    
}
