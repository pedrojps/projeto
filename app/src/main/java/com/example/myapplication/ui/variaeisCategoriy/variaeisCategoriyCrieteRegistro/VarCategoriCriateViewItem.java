package com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class VarCategoriCriateViewItem extends AbstractFlexibleItem<VarCategoriCriateViewHolder> {

    private final ItemCategoria mItemCategoria;

    public VarCategoriCriateViewItem(ItemCategoria obra) {
        this.mItemCategoria = obra;
    }

    public ItemCategoria getModel() {
        return mItemCategoria;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return mItemCategoria.equals(o);
    }

    @Override
    public int hashCode() {
        return mItemCategoria.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_variavel_criate;
    }

    @Override
    public VarCategoriCriateViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemVariavelCriateBinding binding = DataBindingUtil.bind(view);
        return new VarCategoriCriateViewHolder(binding, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, VarCategoriCriateViewHolder holder, int position, List payloads) {
        holder.bind(mItemCategoria);
    }
}
