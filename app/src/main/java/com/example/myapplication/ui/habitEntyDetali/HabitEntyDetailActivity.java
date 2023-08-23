package com.example.myapplication.ui.habitEntyDetali;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActEntyDetalhesDetailBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.entidadeHabitoAdd.AddEditHabitoEntidadeActivity;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.ui.variaeisCategoriy.variaveisCategoriyDetalhe.VarCategoriDetalheViewItem;
import com.example.myapplication.utils.DialogUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;

import static com.example.myapplication.ui.entidadeHabitoAdd.AddEditHabitoEntidadeActivity.REQUEST_EDIT_CODE;

public class HabitEntyDetailActivity
        extends AppCompatActivity {

    public static final int REQUEST_DETAIL_CODE = 4;

    public static final int RESULT_EDIT_OK = RESULT_FIRST_USER + 6;

    public static final int RESULT_DELETE_OK = RESULT_EDIT_OK + 7;

    public static final String EXTRA_APONTAMENTO_ID = "EXTRA_APONTAMENTO_ID";

    private FlexibleAdapter<VarCategoriDetalheViewItem> mAdapter;

    private ActEntyDetalhesDetailBinding mBinding;

    private HabitEntyDetailViewModel mViewModel;

    public static Intent getNewIntent(@NonNull Context context, long apontamentoId) {
        return new Intent(context, HabitEntyDetailActivity.class)
                .putExtra(EXTRA_APONTAMENTO_ID, apontamentoId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_enty_detalhes_detail);
        mBinding.setVm(mViewModel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupAdapters();
        subscribeApontamentoDeleted();
        subscribeCaregaVariaveis();
        subscribeEditApontamento();
        subscribeErrorMessage();

    }

    private void setupAdapters() {

        mAdapter = new FlexibleAdapter<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_habit_e);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
    }


    private void carregaVariavei(){
        mAdapter.clear();
        mAdapter.addItems(0,mViewModel.getVariaveis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_equipamento_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_equipamento_detail_edit:
                mViewModel.editApontamento();
                return true;
            case R.id.menu_equipamento_detail_delete:
                deleteApontamento();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_EDIT_OK && resultCode == RESULT_OK) {
            setResult(RESULT_EDIT_OK);
            finish();
        }
    }

    public void editApontamento(long id) {
        Intent it = AddEditHabitoEntidadeActivity.getNewIntent(this,mViewModel.getApontamento());
        startActivityForResult(it, REQUEST_EDIT_CODE);
    }

    public void deleteApontamento() {

        DialogFactory.createDialog(this, getString(R.string.confirmar_delete_habito))
                .setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, i) -> {
                    mViewModel.delete();
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void subscribeCaregaVariaveis() {
        mViewModel.getCarregaEnty().observe(this,  aVoid -> {
            carregaVariavei();
        });
    }

    private void subscribeApontamentoDeleted() {
        mViewModel.getApontamentoDeleted().observe(this, aVoid -> {
            setResult(RESULT_DELETE_OK);
            finish();
        });
    }

    private void subscribeEditApontamento() {
        mViewModel.getEditApontamento().observe(this, this::editApontamento);
    }

    private void subscribeErrorMessage(){
        mViewModel.getErrorDialogMessage().observe(this, (header, message) ->
                DialogUtils.showDialog(this, header, message)
        );
    }

    private HabitEntyDetailViewModel findOrCreateViewModel() {
        long apontamentoId = getIntent().getLongExtra(EXTRA_APONTAMENTO_ID, 0);

        HabitEntyDetailViewModel.Factory factory = new HabitEntyDetailViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                apontamentoId
        );

        return ViewModelProviders.of(this, factory).get(HabitEntyDetailViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.loadHabitEnty();
    }
}
