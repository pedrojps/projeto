package com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;

import java.util.List;


public class VarCategoriCrieteSpinnerAdapter extends SpinnerAdapter<ItemCategoria, VarCategoriCriateSpinnerViewHolder> {

    public VarCategoriCrieteSpinnerAdapter(@NonNull List<ItemCategoria> data) {
        super(data);
    }

    @Override
    public VarCategoriCriateSpinnerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelCriateBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_criate, parent, false);
        return new VarCategoriCriateSpinnerViewHolder(binding);
    }

    @Override
    public void bindViewHolder(VarCategoriCriateSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
        VarCategoriCrieteSpinnerAdapter v = this;
        holder.getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //v.
            }
        });
    }

    @Override
    public VarCategoriCriateSpinnerViewHolder createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelCriateBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_criate, parent, false);
        return new VarCategoriCriateSpinnerViewHolder(binding);
    }

    @Override
    public void bindDropDownViewHolder(VarCategoriCriateSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    
}
