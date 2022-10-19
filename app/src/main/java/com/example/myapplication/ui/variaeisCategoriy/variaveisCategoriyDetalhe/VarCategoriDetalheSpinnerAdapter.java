package com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.SpinnerAdapter;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;
import com.example.myapplication.databinding.ItemVariavelDetalheBinding;

import java.util.List;


public class VarCategoriDetalheSpinnerAdapter extends SpinnerAdapter<ItemCategoria, VarCategoriDetalheSpinnerViewHolder> {

    public VarCategoriDetalheSpinnerAdapter(@NonNull List<ItemCategoria> data) {
        super(data);
    }

    @Override
    public VarCategoriDetalheSpinnerViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelDetalheBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_detalhe, parent, false);
        return new VarCategoriDetalheSpinnerViewHolder(binding);
    }

    @Override
    public void bindViewHolder(VarCategoriDetalheSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
        VarCategoriDetalheSpinnerAdapter v = this;
        holder.getView().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //v.
            }
        });
    }

    @Override
    public VarCategoriDetalheSpinnerViewHolder createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position) {
        ItemVariavelDetalheBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_variavel_detalhe, parent, false);
        return new VarCategoriDetalheSpinnerViewHolder(binding);
    }

    @Override
    public void bindDropDownViewHolder(VarCategoriDetalheSpinnerViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    
}
