package com.example.myapplication.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.HideFabOnScrollRecyclerViewListener;
import com.example.myapplication.data.network.Status;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.ui.habitCategoriDetali.HabitCategoriaDetailActivity;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.Globals;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

public class MainActivity extends AppCompatActivity  implements FlexibleAdapter.OnItemClickListener {

    private MainViewModel mViewModel;

    private ActivityMainBinding mBinding;

    private HabitAdapterHome<HabitCategoriViewItem> mAdapter;

    private HideFabOnScrollRecyclerViewListener mScrollStateListener;

    private boolean isShowDialog = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setVm(mViewModel);

        mScrollStateListener = new HideFabOnScrollRecyclerViewListener(mBinding.addHabitButton);

        setupAdapter();

        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_MAIN, boolean.class));
    }


    private void setupAdapter() {
        mAdapter = new HabitAdapterHome<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_habit_c);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
        mBinding.habitList.addOnScrollListener(mScrollStateListener);

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
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.searchView.clearFocus();

        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_MAIN, boolean.class));
        if(isShowDialog) {
            DialogUtils.showDialog(this, "Bem-vindo ao app de controle de hábitos!\n\nPara criar o primeiro hábito, clique no botão \"+\" localizado na parte inferior do app. Isso o levará para a tela de criação de hábitos.");
            Globals.sharedInstance().set(Globals.c.SHOW_DIALOG_MAIN, true);
        }
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

        DialogFactory.createDialog(this, R.string.delete_habito, null)
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
                mAdapter.updateDataSet(resource.data, true, true);
                mBinding.searchView.setQuery("", false);
            } else if (resource.status == Status.ERROR) {
                mViewModel.dataAvaliable.set(false);
                DialogUtils.showDialog(this, resource.message.header, resource.message.body);
            }
        });
    }

    private void subscribeHabitAdd() {
        mViewModel.getHabitAdd().observe(this, aVoid -> openAddHabit());
    }

    public void openAddHabit() {
        Intent it = AddEditHabitoCategoriaActivity.getNewIntent(this);
        startActivity(it);
    }

    private MainViewModel findOrCreateViewModel() {
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(this)
        );
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }

    @Override
    public boolean onItemClick(int position) {
        int viewType = mAdapter.getItemViewType(position);

        if(viewType == R.layout.item_habit_c){
            HabitCategoriViewItem viewItem = mAdapter.getItem(position);

            if (ObjectUtils.nonNull(viewItem)) {
                Intent it = HabitCategoriaDetailActivity.getNewIntent(this,viewItem.getModel());
                startActivity(it);

                return true;
            }
        }
        return false;
    }
}