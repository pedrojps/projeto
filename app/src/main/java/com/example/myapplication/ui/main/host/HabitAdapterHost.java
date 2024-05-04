package com.example.myapplication.ui.main.host;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewHolder;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.ui.main.MainListFragment;
import com.example.myapplication.utils.DayOfWeek;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class HabitAdapterHost extends FragmentStateAdapter {

    public HabitAdapterHost(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public HabitAdapterHost(@NonNull Fragment fragment) {
        super(fragment);
    }

    public HabitAdapterHost(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment f;

        if (position==0)
            f = MainListFragment.newInstance(DayOfWeek.Companion.getDayWeekByDate(new LocalDate()));
        else if (position==1)
            f = MainListFragment.newInstance(DayOfWeek.NONE);
        else
            throw new IndexOutOfBoundsException();

        return f;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public String getName(int position) {
        String s;

        if (position==0)
            s= "Hoje";
        else if (position==1)
            s= "Todos";
        else
            throw new IndexOutOfBoundsException();

        return s;
    }
}
