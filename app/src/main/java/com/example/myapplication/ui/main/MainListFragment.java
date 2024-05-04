package com.example.myapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.network.Status;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.ui.habitCategoriDetali.HabitCategoriaDetailActivity;
import com.example.myapplication.utils.DayOfWeek;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.Globals;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

public class MainListFragment extends Fragment  implements FlexibleAdapter.OnItemClickListener {

    DayOfWeek day = DayOfWeek.NONE;

    private final static String TAG_DAY = "TAG_DAY";

    private MainViewModel mViewModel;

    private ActivityMainBinding mBinding;

    private HabitAdapterHome<HabitCategoriViewItem> mAdapter;

    private boolean isShowDialog = false;

    private Globals.c DIALOG = Globals.c.SHOW_DIALOG_MAIN_DAY;

    private String messagem = "Bem-vindo ao app de controle de hábitos!\n\nPara criar o primeiro hábito, clique no botão \"+\" localizado na parte inferior do app. Isso o levará para a tela de criação de hábitos. Há 2 abas de hábitos: os marcados para hoje e a aba de Todos.";

    public static MainListFragment newInstance(DayOfWeek day){
        MainListFragment mainFragment = new MainListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(TAG_DAY, day.getId());
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = ActivityMainBinding.inflate(inflater, container, false);
        mBinding.setVm(mViewModel);

        setupAdapter();
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            day = DayOfWeek.Companion.getById(bundle.getInt(TAG_DAY));
        }

        mViewModel = findOrCreateViewModel();
        mViewModel.setDay(day);
        //if(day == DayOfWeek.NONE){
        //    DIALOG = Globals.c.SHOW_DIALOG_MAIN;
       // }
        //else {
        //    DIALOG = Globals.c.SHOW_DIALOG_MAIN_DAY;
       // }

        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(DIALOG, boolean.class));
    }

    private void setupAdapter() {
        mAdapter = new HabitAdapterHome<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this.getActivity())
                .withDefaultDivider(R.layout.item_habit_c);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);

        subscribeItems();
        subscribeHabitAdd();

        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                isEmpty();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.searchView.clearFocus();

        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(DIALOG, boolean.class));
        if(isShowDialog) {
            DialogUtils.showDialog(this.getActivity(), messagem);
            Globals.sharedInstance().set(DIALOG, true);
        }
    }

    private void subscribeItems() {
        mViewModel.getItems().observe(this.getActivity(), resource -> {
            if (resource.status == Status.SUCCESS) {
                mViewModel.dataAvaliable.set(ObjectUtils.nonNull(resource.data) && !resource.data.isEmpty());
                mAdapter.updateDataSet(resource.data, true, true);
                mBinding.searchView.setQuery("", false);
                isEmpty();
            } else if (resource.status == Status.ERROR) {
                mViewModel.dataAvaliable.set(false);
                DialogUtils.showDialog(this.getActivity(), resource.message.header, resource.message.body);
            }
        });
    }

    private void isEmpty(){
        if (mAdapter.mItens.size()==0)
            mBinding.constraintEmpty.setVisibility(View.VISIBLE);
        else
            mBinding.constraintEmpty.setVisibility(View.GONE);
    }

    private void subscribeHabitAdd() {
        mViewModel.getHabitAdd().observe(this, aVoid -> openAddHabit());
    }

    public void openAddHabit() {
        Intent it = AddEditHabitoCategoriaActivity.getNewIntent(this.getActivity());
        startActivity(it);
    }

    private MainViewModel findOrCreateViewModel() {
        MainViewModel.Factory factory = new MainViewModel.Factory(
                this.getActivity().getApplication(),
                Injection.HabitCategoriRepository(this.getActivity())
        );
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public boolean onItemClick(int position) {
        int viewType = mAdapter.getItemViewType(position);

        if(viewType == R.layout.item_habit_c){
            HabitCategoriViewItem viewItem = mAdapter.getItem(position);

            if (ObjectUtils.nonNull(viewItem)) {
                Intent it = HabitCategoriaDetailActivity.getNewIntent(this.getActivity(),viewItem.getModel());
                startActivity(it);

                return true;
            }
        }
        return false;
    }

}
