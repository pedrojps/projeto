package com.example.myapplication.ui.variaeisCategoriy;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCBinding;

import java.util.List;


public class VarCategoriSpinnerAdapter extends SpinnerAdapter<ItemCategoria, VarCategoriSpinnerViewHolder> {

    public VarCategoriSpinnerAdapter(@NonNull List<ItemCategoria> data) {
        super(data);
    }

    @Override
    public VarCategoriSpinnerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelCBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_c, parent, false);
        return new VarCategoriSpinnerViewHolder(binding);
    }

    @Override
    public void bindViewHolder(VarCategoriSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
        VarCategoriSpinnerAdapter v = this;
        holder.getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //v.
            }
        });
    }

    @Override
    public VarCategoriSpinnerViewHolder createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelCBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_c, parent, false);
        return new VarCategoriSpinnerViewHolder(binding);
    }

    @Override
    public void bindDropDownViewHolder(VarCategoriSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    
}
