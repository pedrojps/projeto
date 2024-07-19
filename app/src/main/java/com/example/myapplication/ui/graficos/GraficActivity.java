package com.example.myapplication.ui.graficos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.AlertCategori;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ActGraficoHabitBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.addEdithabit.adapter.AlertAdapter;
import com.example.myapplication.ui.dialog.SimpleDateDialog;
import com.example.myapplication.ui.graficos.adapter.AdapterGrafic;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.Globals;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraficActivity extends AppCompatActivity {

    private GraficViewModel mViewModel;

    private ActGraficoHabitBinding mBinding;

    private AdapterGrafic mAlertAdapter = new AdapterGrafic();

    public static final String EXTRA_HABIT = "EXTRA_HABIT";

    public static Intent getNewIntent(@NonNull Context context, HabitCategoria HabitCategoria) {
        return new Intent(context, GraficActivity.class)
                .putExtra(EXTRA_HABIT, HabitCategoria);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.act_grafico_habit);
        mBinding.setVm(mViewModel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        subscribeChangeEndDate();
        subscribeChangeStartDate();
        subscribeCaregaVariaveis();
        subscribeAlert();

        mBinding.title.setText(getString(R.string.habit_nome, mViewModel.getHabitName()));
    }

    private void subscribeCaregaVariaveis() {
        mViewModel.getCarregaEnty().observe(this,  aVoid -> {
            String text = mViewModel.aditionalText();
            mBinding.vezes.setText(getString(R.string.habit_periodo, mViewModel.getSizeList()+""));
            setDate(mViewModel.setData());

            if (text != null)
                mBinding.variaveisAdicionais.setText(text);
            else
                mBinding.infoVezes.setVisibility(View.GONE);

           mAlertAdapter.setListItems(new ArrayList(mViewModel.getListGrafic()),mViewModel.startDate.get(),mViewModel.endDate.get());
           if (!mViewModel.getTextVariabe().isEmpty()){
               mBinding.variaveisText.setVisibility(View.VISIBLE);
               mBinding.variaveisText.setText(mViewModel.getTextVariabe());
           } else
                mBinding.variaveisText.setVisibility(View.GONE);
        });
    }

    private void subscribeAlert(){
        mBinding.listGraficItem.setLayoutManager(new LinearLayoutManager(this));
        mBinding.listGraficItem.setAdapter(mAlertAdapter);
        mBinding.listGraficItem.setNestedScrollingEnabled(false);
    }

    private void setDate(List<Entry> list){
        LineDataSet d = new LineDataSet(list,"");
        LineData data = new LineData(d);

        mBinding.lineChart.setData(data);
        mBinding.lineChart.notifyDataSetChanged(); // let the chart know it's data changed
        mBinding.lineChart.invalidate(); // refresh chart
    }

    private void subscribeChangeStartDate() {
        mViewModel.getChangeStartDate().observe(this, aVoid ->
                new SimpleDateDialog(this, mViewModel.startDate.get(), mViewModel::setStartDate).show());

    }

    private void subscribeChangeEndDate() {
        mViewModel.getChangeEndDate().observe(this, aVoid ->
                new SimpleDateDialog(this, mViewModel.endDate.get(), mViewModel::setEndDate).show());

    }

    private GraficViewModel findOrCreateViewModel() {
        HabitCategoria habitCategoria = getIntent().getParcelableExtra(EXTRA_HABIT);

        GraficViewModel.Factory factory = new GraficViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(getApplicationContext()),
                habitCategoria
        );

        return ViewModelProviders.of(this, factory).get(GraficViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_GRAFIC, boolean.class));
        if(isShowDialog) {
            DialogUtils.showDialog(this, "Aqui você consegue ver as informações dos seus hábitos dentro de um período. Basta clicar na data para mudar o período. As informações marcadas como valor numérico mostram uma média abaixo.");
            Globals.sharedInstance().set(Globals.c.SHOW_DIALOG_GRAFIC, true);
        }
    }
}
