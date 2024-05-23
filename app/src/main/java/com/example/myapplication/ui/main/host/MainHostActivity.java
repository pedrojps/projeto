package com.example.myapplication.ui.main.host;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.data.network.Status;
import com.example.myapplication.data.notification.NotificationUtils;
import com.example.myapplication.databinding.ActivityMainHostBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.about.AboutActivity;
import com.example.myapplication.ui.addEdithabit.AddEditHabitoCategoriaActivity;
import com.example.myapplication.ui.factory.DialogFactory;
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem;
import com.example.myapplication.ui.habitCategoriDetali.HabitCategoriaDetailActivity;
import com.example.myapplication.ui.main.MainViewModel;
import com.example.myapplication.utils.BatteryOptimizationUtils;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.Globals;
import com.example.myapplication.utils.ObjectUtils;
import com.example.myapplication.utils.adapters.mediators.TabLayoutSimpleBaseMediator;

import java.util.List;

public class MainHostActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    private ActivityMainHostBinding mBinding;

    long scheduleId = -1;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permissão concedida, agenda notificações
                    NotificationUtils.INSTANCE.scheduleNotifications(MainHostActivity.this);
                } else {
                    // Permissão negada, lidar com o caso
                    Globals.sharedInstance().set(Globals.c.SHOW_ALERT_OPISION, true);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_host);
        mBinding.setVm(mViewModel);
        subscribeItems();
        setup();
        requestPermission();
        // Verificar e solicitar remoção das otimizações de bateria
        if (!BatteryOptimizationUtils.INSTANCE.isIgnoringBatteryOptimizations(this)) {
            BatteryOptimizationUtils.INSTANCE.showBatteryOptimizationDialog(this);
        }
    }

    private void requestPermission(){
        // Verificar e solicitar permissão de notificação se necessário
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Permissão já concedida, agenda notificações
                NotificationUtils.INSTANCE.scheduleNotifications(this);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                // Mostrar um racional explicando por que a permissão é necessária
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                // Solicitar a permissão diretamente
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // Para versões anteriores ao Android 13, agenda notificações diretamente
            NotificationUtils.INSTANCE.scheduleNotifications(this);
        }
    }

    private void redirect(List<HabitCategoriViewItem> data) {
        if (scheduleId == -1) return;
        HabitCategoria habito = null;
        for (HabitCategoriViewItem schedule : data)
            if (schedule.getModel().getId() == scheduleId){
                habito = schedule.getModel();
            }
        if (habito==null) return;
        Intent it = HabitCategoriaDetailActivity.getNewIntent(this,habito, true);
        startActivity(it);
        scheduleId = -1;
    }

    private void setup() {
        HabitAdapterHost adapter = new HabitAdapterHost(this);
        mBinding.pages.setOffscreenPageLimit(adapter.getItemCount());
        mBinding.pages.setAdapter(adapter);

        new TabLayoutSimpleBaseMediator(
                mBinding.categoryTabs,
                mBinding.pages,
                (tab, position) -> tab.setText(adapter.getName(position))
        ).attach();
        
        subscribeHabitAdd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.INSTANCE.scheduleNotifications(this);

        Intent intent = getIntent();
        scheduleId = intent.getLongExtra("scheduleId", -1L);
        intent.putExtra("scheduleId", -1L);
/*
        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_MAIN_C, boolean.class));
        if(isShowDialog) {
            DialogUtils.showDialog(this, "Bem-vindo ao app de controle de hábitos!\n\nPara criar o primeiro hábito, clique no botão \"+\" localizado na parte inferior do app. Isso o levará para a tela de criação de hábitos.");
            Globals.sharedInstance().set(Globals.c.SHOW_DIALOG_MAIN_C, true);
        }*/
    }
    private void subscribeItems() {
        mViewModel.getItems().observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                mViewModel.dataAvaliable.set(ObjectUtils.nonNull(resource.data) && !resource.data.isEmpty());
                redirect(resource.data);
            } else if (resource.status == Status.ERROR) {
                mViewModel.dataAvaliable.set(false);
                DialogUtils.showDialog(this, resource.message.header, resource.message.body);
            }
        });
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
                openAbout();
                return true;
            case R.id.menu_item_habit_list_delete:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void delete() {
        mViewModel.clearItemSelectionMap();
        if (mViewModel.getDisplayNameArray().length!=0)
            DialogFactory.createDialog(this, R.string.delete_habito, null)
                    .setMultiChoiceItems(mViewModel.getDisplayNameArray(), null,
                            (dialogInterface, which, isChecked) -> mViewModel.toggleItemSelection(which, isChecked))
                    .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, which) -> {
                        mViewModel.deleteSelected();
                        dialogInterface.dismiss();
                    })
                    .setNegativeButton(R.string.dialog_button_cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        else
            DialogUtils.showDialog(this, "Não há hábitos a serem excluídos!");
    }

    private void subscribeHabitAdd() {
        mViewModel.getHabitAdd().observe(this, aVoid -> openAddHabit());
    }

    public void openAddHabit() {
        Intent it = AddEditHabitoCategoriaActivity.getNewIntent(this);
        startActivity(it);
    }
    public void openAbout() {
        Intent it = new Intent(this, AboutActivity.class);
        startActivity(it);
    }
    private MainViewModel findOrCreateViewModel() {
        MainViewModel.Factory factory = new MainViewModel.Factory(
                getApplication(),
                Injection.HabitCategoriRepository(this)
        );
        return ViewModelProviders.of(this, factory).get(MainViewModel.class);
    }


}