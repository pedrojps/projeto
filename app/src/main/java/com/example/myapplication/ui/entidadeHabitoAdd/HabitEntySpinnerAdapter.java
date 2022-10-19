package com.example.myapplication.ui.entidadeHabitoAdd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.databinding.ItemHabitEBinding;

import java.util.List;


public class HabitEntySpinnerAdapter extends SpinnerAdapter<HabitEnty, HabitEntySpinnerViewHolder> {

    public HabitEntySpinnerAdapter(@NonNull List<HabitEnty> data) {
        super(data);
    }

    @Override
    public HabitEntySpinnerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemHabitEBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_habit_e, parent, false);
        return new HabitEntySpinnerViewHolder(binding);
    }

    @Override
    public void bindViewHolder(HabitEntySpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
        HabitEntySpinnerAdapter v = this;
        holder.getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //v.
            }
        });
    }

    @Override
    public HabitEntySpinnerViewHolder createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemHabitEBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_habit_e, parent, false);
        return new HabitEntySpinnerViewHolder(binding);
    }

    @Override
    public void bindDropDownViewHolder(HabitEntySpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    
}
