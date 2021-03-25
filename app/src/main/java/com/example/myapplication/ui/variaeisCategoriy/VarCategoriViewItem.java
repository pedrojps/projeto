package com.example.myapplication.ui.variaeisCategoriy;

import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ItemHabitCBinding;
import com.example.myapplication.databinding.ItemVariavelCBinding;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class VarCategoriViewItem extends AbstractFlexibleItem<VarCategoriViewHolder> {

    private final ItemCategoria mProjeto;

    public VarCategoriViewItem(ItemCategoria obra) {
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
        return R.layout.item_variavel_c;
    }

    @Override
    public VarCategoriViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        ItemVariavelCBinding binding = DataBindingUtil.bind(view);
        return new VarCategoriViewHolder(binding, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, VarCategoriViewHolder holder, int position, List payloads) {
        holder.bind(mProjeto);
    }
}
