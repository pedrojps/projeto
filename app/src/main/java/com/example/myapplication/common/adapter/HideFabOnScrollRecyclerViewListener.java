package com.example.myapplication.common.adapter;



import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import eu.davidea.fastscroller.FastScroller;

public class HideFabOnScrollRecyclerViewListener
        extends RecyclerView.OnScrollListener
        implements FastScroller.OnScrollStateChangeListener{

    private final FloatingActionButton[] mFabArray;

    public HideFabOnScrollRecyclerViewListener(FloatingActionButton... fabsToHide) {
        mFabArray = fabsToHide;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(dy > 0){
            hideFabs();
        }else if(dy < 0){
            showFabs();
        }
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onFastScrollerStateChange(boolean scrolling) {
        if(scrolling) hideFabs();
        else showFabs();
    }

    private void hideFabs(){
        for (FloatingActionButton fab : mFabArray) {
            if(fab.isShown())
                fab.hide();
        }
    }

    private void showFabs(){
        for (FloatingActionButton fab : mFabArray) {
            if(!fab.isShown())
                fab.show();
        }
    }

}
