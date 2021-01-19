package com.example.myapplication.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.HideFabOnScrollRecyclerViewListener;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.network.Status;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    private ActivityMainBinding mBinding;

    private FlexibleAdapter<HabitCategoriViewItem> mAdapter;


    private HideFabOnScrollRecyclerViewListener mScrollStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setVm(mViewModel);

        mScrollStateListener = new HideFabOnScrollRecyclerViewListener(mBinding.addHabitButton);

        setupAdapter();
    }


    private void setupAdapter() {
        mAdapter = new FlexibleAdapter<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_habit_c);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
        mBinding.habitList.addOnScrollListener(mScrollStateListener);

        subscribeItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_item_about:
                //onBackPressed();
                return true;
            case R.id.menu_item_habit_list_delete:
                deleteProjetos();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteProjetos() {
        mViewModel.clearItemSelectionMap();

        DialogFactory.createDialog(this, R.string.list_projeto_delete_projeto_dialog_title, null)
                .setMultiChoiceItems(mViewModel.getProjetoDisplayNameArray(), null,
                        (dialogInterface, which, isChecked) -> mViewModel.toggleItemSelection(which, isChecked))
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, which) -> {
                    mViewModel.deleteProjetosSelected();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void subscribeItems() {
        mViewModel.getItems().observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                mViewModel.dataAvaliable.set(ObjectUtils.nonNull(resource.data) && !resource.data.isEmpty());
                mAdapter.updateDataSet(resource.data, true);
            } else if (resource.status == Status.ERROR) {
                mViewModel.dataAvaliable.set(false);
                DialogUtils.showDialog(this, resource.message.header, resource.message.body);
            }
        });
    }

    private MainViewModel findOrCreateViewModel() {
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(this)
        );
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }
}