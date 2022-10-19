package com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemVariavelCriateBinding;
import com.example.myapplication.databinding.ItemVariavelDetalheBinding;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class VarCategoriDetalheViewItem extends AbstractFlexibleItem<VarCategoriDetalheViewHolder> {

    private final ItemCategoria mProjeto;

    public VarCategoriDetalheViewItem(ItemCategoria obra) {
        this.mProjeto = obra;
    }

    public ItemCategoria getModel() {
        return mProjeto;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return mProjeto.equals(o);
    }

    @Override
    public int hashCode() {
        return mProjeto.hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_variavel_detalhe;
    }

    @Override
    public VarCategoriDetalheViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemVariavelDetalheBinding binding = DataBindingUtil.bind(view);
        return new VarCategoriDetalheViewHolder(binding, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, VarCategoriDetalheViewHolder holder, int position, List payloads) {
        holder.bind(mProjeto);
    }
}
