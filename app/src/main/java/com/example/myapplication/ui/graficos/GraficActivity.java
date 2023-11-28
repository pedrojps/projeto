package com.example.myapplication.ui.graficos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ActGraficoHabitBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.dialog.SimpleDateDialog;

public class GraficActivity extends AppCompatActivity {

    private GraficViewModel mViewModel;

    private ActGraficoHabitBinding mBinding;

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

        mBinding.title.setText(getString(R.string.habit_nome, mViewModel.getHabitName()));
    }

    private void subscribeCaregaVariaveis() {
        mViewModel.getCarregaEnty().observe(this,  aVoid -> {
            mBinding.vezes.setText(getString(R.string.habit_periodo, mViewModel.getSizeList()+""));
            mBinding.variaveisAdicionais.setText(mViewModel.aditionalText());
        });
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
}
