package com.example.myapplication.ui.entidadeHabitoAdd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.common.time.LocalDate;
import com.example.myapplication.common.time.LocalTime;
import com.example.myapplication.data.entities.HabitEnty;
import com.example.myapplication.data.entities.ItemCategoria;
import com.example.myapplication.databinding.ActHabitEntidadeAddEditBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.dialog.DialogAddHabitItemActivity;
import com.example.myapplication.ui.dialog.SimpleDateDialog;
import com.example.myapplication.ui.dialog.SimpleTimeDialog;
import com.example.myapplication.ui.variaeisCategoriy.variaeisCategoriyCrieteRegistro.VarCategoriCriateViewItem;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;


public class AddEditHabitoEntidadeActivity extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener  {

    public static final int REQUEST_ADD_CODE = 17;

    public static final int REQUEST_EDIT_CODE = REQUEST_ADD_CODE + 15;

    private static final String EXTRA_HABITY_CATEGORI = "EXTRA_HABITY_CATEGORI";

    private static final String EXTRA_HABITY_TIME = "EXTRA_HABITY_TIME";

    private static final String EXTRA_HABITY_EDIT = "EXTRA_HABITY_EDIT";

    private AddEditHabitoEntidadeViewModel mViewModel;

    private ActHabitEntidadeAddEditBinding mBinding;

    private FlexibleAdapter<VarCategoriCriateViewItem> mAdapter;

    @NonNull
    public static Intent getNewIntent(Context context, @NonNull String id, long date) {
        return new Intent(context, AddEditHabitoEntidadeActivity.class)
                .putExtra(EXTRA_HABITY_CATEGORI, id)
                .putExtra(EXTRA_HABITY_TIME, date);
    }

    @NonNull
    public static Intent getNewIntent(Context context, @NonNull HabitEnty habitEnty) {
        return new Intent(context, AddEditHabitoEntidadeActivity.class)
                .putExtra(EXTRA_HABITY_EDIT, habitEnty);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_habit_entidade_add_edit);
        mBinding.setVm(mViewModel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupAdapters();
        subscribeViewChanges();

        initViewData();
    }

    private void initViewData() {
        boolean isUpdate = getIntent().hasExtra(EXTRA_HABITY_EDIT);
        //mAdapter.addItem(new VarCategoriViewItem(new ItemCategoria(0,new DateTime(),"teste")));
        if (isUpdate) {
            HabitEnty h =getIntent().getParcelableExtra(EXTRA_HABITY_EDIT);
            mViewModel.start(h);
        } else {
            mViewModel.start(getIntent().getStringExtra(EXTRA_HABITY_CATEGORI));
            LocalDate ld = new LocalDate(getIntent().getLongExtra(EXTRA_HABITY_TIME,new LocalDate().getTime()));
            mViewModel.startDate.set(ld);
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
        Intent it = DialogAddHabitItemActivity.getNewIntent(this  );
        startActivityForResult(it, DialogAddHabitItemActivity.REQUEST_SELECT_CAMINHO);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(DialogAddHabitItemActivity.REQUEST_SELECT_CAMINHO==requestCode){
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
        subscribeChangeStartDate();
        subscribeChangeStartTime();
        subscribeChangeSalve();
    }

    private void setupAdapters() {

        mAdapter = new FlexibleAdapter<>(new ArrayList<>(), this, true);

        FlexibleItemDecoration itemDecoration = new FlexibleItemDecoration(this)
                .withDefaultDivider(R.layout.item_variavel_c);

        mBinding.habitList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.habitList.setAdapter(mAdapter);
        mBinding.habitList.addItemDecoration(itemDecoration);
    }


    private void subscribeChangeStartDate() {
        mViewModel.getChangeStartDate().observe(this, aVoid ->
                new SimpleDateDialog(this, mViewModel.startDate.get(), mViewModel::setStartDate).show());

    }

    private void subscribeChangeSalve() {
        mViewModel.getChangeSalve().observe(this, aVoid -> mViewModel.save(mAdapter.getCurrentItems()));

    }

    private void subscribeChangeStartTime() {
        mViewModel.getChangeStartTime().observe(this, aVoid ->
                new SimpleTimeDialog(this, mViewModel.startTime.get(), mViewModel::setStartTime).show());

    }

    private AddEditHabitoEntidadeViewModel findOrCreateViewModel() {
        AddEditHabitoEntidadeViewModel.Factory factory = new AddEditHabitoEntidadeViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                Injection.VariavelCategoriRepository(getApplicationContext())
        );
        return ViewModelProviders.of(this, factory).get(AddEditHabitoEntidadeViewModel.class);
    }

    @Override
    public boolean onItemClick(int position) {
        int viewType = mAdapter.getItemViewType(position);

        if(viewType == R.layout.item_variavel_criate){
            VarCategoriCriateViewItem viewItem = mAdapter.getItem(position);
            if (ObjectUtils.nonNull(viewItem)) {
                if(viewItem.getModel().getTipo()==0){
                    List<VarCategoriCriateViewItem> list= mAdapter.getCurrentItems();
                    new SimpleTimeDialog(this, viewItem.getModel().time.get(), time->{
                        List<VarCategoriCriateViewItem> novo = new ArrayList<>();
                        int i=0;
                        for (VarCategoriCriateViewItem v : list){
                            if(position==i){
                                ItemCategoria ic = v.getModel();
                                ic.time.set(new LocalTime(time));
                                novo.add(new VarCategoriCriateViewItem(ic));
                            }else
                                novo.add(v);
                            i++;
                        }
                        mAdapter.updateDataSet(novo);
                    }).show();
                }

                return false;
            }
        }
        return false;
    }

}
