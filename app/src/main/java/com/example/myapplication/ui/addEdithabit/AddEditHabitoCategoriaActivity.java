package com.example.myapplication.ui.addEdithabit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.common.adapter.HideFabOnScrollRecyclerViewListener;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.databinding.ActHabitCategoriaAddEditBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.dialog.DialogActivity;
import com.example.myapplication.ui.variaeisCategoriy.VarCategoriViewItem;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;


public class AddEditHabitoCategoriaActivity extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener  {

    public static final int REQUEST_ADD_CODE = 17;

    public static final int REQUEST_EDIT_CODE = REQUEST_ADD_CODE + 15;

    private static final String EXTRA_HABITY_CATEGORI_ID = "EXTRA_HABITY_CATEGORI_ID";

    private AddEditHabitoCategoriaViewModel mViewModel;

    private ActHabitCategoriaAddEditBinding mBinding;

    private FlexibleAdapter<VarCategoriViewItem> mAdapter;

    @NonNull
    public static Intent getNewIntent(Context context) {
        return new Intent(context, AddEditHabitoCategoriaActivity.class);
    }

    @NonNull
    public static Intent getNewIntent(Context context, @NonNull HabitCategoria equipamentoId) {
        return new Intent(context, AddEditHabitoCategoriaActivity.class)
                .putExtra(EXTRA_HABITY_CATEGORI_ID, equipamentoId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_habit_categoria_add_edit);
        mBinding.setVm(mViewModel);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        setupAdapters();
        subscribeViewChanges();

        initViewData();
    }

    private void initViewData() {
        boolean isUpdate = getIntent().hasExtra(EXTRA_HABITY_CATEGORI_ID);

        if (isUpdate) {
            HabitCategoria c = getIntent().getParcelableExtra(EXTRA_HABITY_CATEGORI_ID);
            mViewModel.start(c);
        } else {
            mViewModel.start();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void equipamentoSaved() {
        setResult(RESULT_OK);
        finish();
    }

    public void variavelDialogAdd() {
        Intent it = DialogActivity.getNewIntent(this  );
        startActivityForResult(it, DialogActivity.REQUEST_SELECT_CAMINHO);
    }

    private void subscribeErrorMessage() {
        mViewModel.getErrorDialogMessage().observe(this, (header, message) ->
                DialogUtils.showDialog(this, header, message)
        );
    }

    private void subscribeEquipamentoSaved() {
        mViewModel.getEquipamentoSavedEvent().observe(this, aVoid -> equipamentoSaved());
    }
    private void subscribeAddVariavel() {
        mViewModel.getVariaveisAdd().observe(this, aVoid -> variavelDialogAdd());
    }

    private void subscribeCarregaVariavel() {
        mViewModel.getVariavelCarrega().observe(this, aVoid -> carregaVariavei());
    }

    private void subscribeDeletVarivaelAntiga() {
        mViewModel.getDeletVariavelAntiga().observe(this, aVoid -> mAdapter.removeItem(mViewModel.posisaoAdapt));
    }

    private void subscribeFalha(){
        mViewModel.getFalha().observe(this, aVoid ->falha());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(DialogActivity.REQUEST_SELECT_CAMINHO==requestCode){
        mAdapter.addItem(mViewModel.handleEditRequisitanteResult(resultCode,data));
        }

    }
    private void carregaVariavei(){
        mAdapter.addItems(0,mViewModel.getVariaveis());
    }


    private void subscribeViewChanges() {
        subscribeEquipamentoSaved();
        subscribeErrorMessage();
        subscribeAddVariavel();
        subscribeCarregaVariavel();
        subscribeDeletVarivaelAntiga();
        subscribeFalha();
    }

    private void setupAdapters() {

        mAdapter = new FlexibleAdapter<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_variavel_c);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
    }

    private AddEditHabitoCategoriaViewModel findOrCreateViewModel() {
        AddEditHabitoCategoriaViewModel.Factory factory = new AddEditHabitoCategoriaViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                Injection.VariavelCategoriRepository(getApplicationContext())
        );
        return ViewModelProviders.of(this, factory).get(AddEditHabitoCategoriaViewModel.class);
    }

    private void falha(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.excluir);
        builder.setMessage("Falha ao carregar/salvar dados");
        builder.setPositiveButton(R.string.fwandroid_dialog_button_ok, (dialogInterface, i) -> {
            onBackPressed();
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    @Override
    public boolean onItemClick(int position) {
        int viewType = mAdapter.getItemViewType(position);

        if(viewType == R.layout.item_variavel_c){
            VarCategoriViewItem viewItem = mAdapter.getItem(position);

            if (ObjectUtils.nonNull(viewItem)) {

                if(mViewModel.isNewEquipamento())
                    mAdapter.removeItem(position);
                mViewModel.removerItens(viewItem.getModel(),position);

                return false;
            }
        }
        return false;
    }

}
