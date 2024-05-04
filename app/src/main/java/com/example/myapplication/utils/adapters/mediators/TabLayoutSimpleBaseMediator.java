package com.example.myapplication.utils.adapters.mediators;

import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.viewpager2.widget.ViewPager2;

public class TabLayoutSimpleBaseMediator {

    protected TabLayout tabLayout;
    protected ViewPager2 viewPager2;
    private final Config config;
    TabLayoutSimpleBaseMediator INSTANCE = this;

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            INSTANCE.onTabSelected(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    private final ViewPager2.OnPageChangeCallback onPageChangeListener = new ViewPager2.OnPageChangeCallback(){

        @Override
        public void onPageSelected(int position) {
            INSTANCE.onPageSelected(position);
        }
    };

    public TabLayoutSimpleBaseMediator(
            TabLayout tableLayout,
            ViewPager2 viewPager2,
            Config config){

        this.tabLayout = tableLayout;
        this.viewPager2 = viewPager2;
        this.config = config;
    }

    public void attach(){
        Adapter adapter = viewPager2.getAdapter();
        if (adapter == null) return;

        int cont = adapter.getItemCount();
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener);
        viewPager2.unregisterOnPageChangeCallback(onPageChangeListener);
        tabLayout.removeAllTabs();

        for (int i = 0; i<cont;i++){
            TabLayout.Tab tab = tabLayout.newTab();

            config.config(tab,i);
            onTabCreated(tab,i);
            tabLayout.addTab(tab);
        }


        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        viewPager2.registerOnPageChangeCallback(onPageChangeListener);
    }

    protected void onTabCreated(TabLayout.Tab tab, int position) {}

    protected void onTabSelected(TabLayout.Tab tab) {
        if (tab==null) return;

        viewPager2.setCurrentItem(tab.getPosition());
    }

    protected void onPageSelected(int position) {
        TabLayout.Tab tab= tabLayout.getTabAt(position);
        if (tab!= null) tab.select();
    }

    public interface Config {

        public void config(TabLayout.Tab tab, int position);
    }
}
