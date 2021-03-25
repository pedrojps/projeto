package com.example.myapplication.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.myapplication.data.entities.ItemCategoria;

import java.util.ArrayList;
import java.util.List;

public abstract class SpinnerAdapter<T, VH extends SpinnerAdapter.ISpinnerViewHolder<T>>
        extends BaseAdapter
        implements MutableSpinnerAdapter<T> {

    public interface ISpinnerViewHolder<E> {

        void bind(E data);

        View getView();
    }

    public static final int NO_POSITION = -1;

    private final List<T> mDataSet = new ArrayList<>();

    private int mBackupPosition = NO_POSITION;

    public SpinnerAdapter(
            @NonNull List<T> data
    ) {
        setDatSet(data);
    }

    @Override
    public void updateDataSet(List<T> data) {
        setDatSet(data);
    }

    private void setDatSet(List<T> data) {
        mDataSet.clear();
        mDataSet.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : mDataSet.size();
    }

    @Override
    public T getItem(int position) {
        return isEmpty() || position > getCount() ? null : mDataSet.get(position);
    }

    public int getItemPosition(T item){
        return isEmpty() || item == null ? NO_POSITION : mDataSet.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return mDataSet == null || mDataSet.isEmpty();
    }

    @Override
    public void setItemSelected(Spinner spinner, T item) {
        setItemSelected(spinner, getItemPosition(item));
    }

    public void setItemSelected(Spinner spinner, int position){
        if(position != NO_POSITION && position != mBackupPosition && position < getCount()){
            spinner.setSelection(position, true);
            mBackupPosition = position;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = createViewHolder(inflater, parent, position);
            convertView = holder.getView();
            convertView.setTag(holder);
        } else {
            //noinspection unchecked
            holder = (VH) convertView.getTag();
        }

        bindViewHolder(holder, position);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        VH holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            holder = createDropDownViewHolder(inflater, parent, position);
            convertView = holder.getView();
            convertView.setTag(holder);
        } else {
            //noinspection unchecked
            holder = (VH) convertView.getTag();
        }

        bindDropDownViewHolder(holder, position);
        return convertView;
    }

    public abstract VH createViewHolder(LayoutInflater inflater, ViewGroup parent, int position);

    public abstract void bindViewHolder(VH holder, int position);

    public abstract VH createDropDownViewHolder(LayoutInflater inflater, ViewGroup parent, int position);

    public abstract void bindDropDownViewHolder(VH holder, int position);

}