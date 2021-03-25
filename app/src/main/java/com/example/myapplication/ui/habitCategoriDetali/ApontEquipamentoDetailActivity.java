package com.example.myapplication.ui.habitCategoriDetali;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActCategoriDetalhesDetailBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.utils.DialogUtils;

import java.util.EventListener;

import static com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity.REQUEST_EDIT_CODE;

public class ApontEquipamentoDetailActivity
        extends AppCompatActivity{

    public static final int REQUEST_DETAIL_CODE = 4;

    public static final int RESULT_EDIT_OK = RESULT_FIRST_USER + 6;

    public static final int RESULT_DELETE_OK = RESULT_EDIT_OK + 7;

    public static final String EXTRA_APONTAMENTO_ID = "EXTRA_APONTAMENTO_ID";

    private CalendarView calendarView;

    private ActCategoriDetalhesDetailBinding mBinding;

    private ApontEquipamentoDetailViewModel mViewModel;

    public static Intent getNewIntent(@NonNull Context context, long apontamentoId) {
        return new Intent(context, ApontEquipamentoDetailActivity.class)
                .putExtra(EXTRA_APONTAMENTO_ID, apontamentoId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_categori_detalhes_detail);
        mBinding.setVm(mViewModel);
        calendarView = (CalendarView) findViewById(R.id.simpleCalendarView);
       // calendarView.addView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        subscribeApontamentoDeleted();
        subscribeEditApontamento();
        subscribeErrorMessage();
        subscribeHabitAdd();
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
            case R.id.menu_equipamento_detail_delete:
                mViewModel.editApontamento();
                return true;
            case R.id.menu_equipamento_detail_edit:
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
        Intent it = AddEditHabitoCategoriaActivity.getNewIntent(this, id+"");
        startActivityForResult(it, REQUEST_EDIT_CODE);
    }

    public void deleteApontamento() {

        DialogFactory.createDialog(this, getString(R.string.confirmation_delete_apontamento_message))
                .setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, i) -> {
                    mViewModel.delete();
                    dialogInterface.dismiss();
                })
                .show();
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
    private void subscribeHabitAdd() {
        mViewModel.getHabitAdd().observe(this, aVoid -> openAddHabit());
    }

    public void openAddHabit() {
        Intent it = com.example.myapplication.ui.entidadeHabitoAdd.AddEditHabitoCategoriaActivity.getNewIntent(this,mViewModel.getApontamento().getId()+"");
        startActivity(it);
    }
    private ApontEquipamentoDetailViewModel findOrCreateViewModel() {
        long apontamentoId = getIntent().getLongExtra(EXTRA_APONTAMENTO_ID, 0);

        ApontEquipamentoDetailViewModel.Factory factory = new ApontEquipamentoDetailViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                apontamentoId
        );

        return ViewModelProviders.of(this, factory).get(ApontEquipamentoDetailViewModel.class);
    }
}
