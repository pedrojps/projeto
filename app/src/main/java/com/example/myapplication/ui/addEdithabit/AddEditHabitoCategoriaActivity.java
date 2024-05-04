package com.example.myapplication.ui.addEdithabit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.activity.result.ActivityResultLauncher;
import com.example.myapplication.R;
import com.example.myapplication.data.entities.HabitCategoria;
import com.example.myapplication.databinding.ActHabitCategoriaAddEditBinding;
import com.example.myapplication.di.Injection;
import com.example.myapplication.ui.dialog.DialogAddHabitItemActivity;
import com.example.myapplication.ui.variaeisCategoriy.VarCategoriViewItem;
import com.example.myapplication.utils.DayOfWeek;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.Globals;
import com.example.myapplication.utils.ImageUtil;
import com.example.myapplication.utils.ObjectUtils;

import java.util.ArrayList;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;


public class AddEditHabitoCategoriaActivity extends AppCompatActivity implements FlexibleAdapter.OnItemClickListener  {

    public static final int REQUEST_ADD_CODE = 17;

    public static final int REQUEST_EDIT_CODE = REQUEST_ADD_CODE + 15;

    private static final String EXTRA_HABITY_CATEGORI_ID = "EXTRA_HABITY_CATEGORI_ID";

    private static final String PERMISSION_GALERIA = ImageUtil.INSTANCE.permissionReadImage();

    private AddEditHabitoCategoriaViewModel mViewModel;

    private ActHabitCategoriaAddEditBinding mBinding;

    private FlexibleAdapter<VarCategoriViewItem> mAdapter;
    private boolean isShowDialog = false;


    public ActivityResultLauncher<String> requestGaleria = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>(){

        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                resultGaleria.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            }else {
                showDialogPermission();
            }
        }
    });

    public ActivityResultLauncher<Intent> resultGaleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
        @Override
        public void onActivityResult(ActivityResult result) {
            Bitmap imageBitMap = null;
            try {
                if (Build.VERSION.SDK_INT < 28){
                        imageBitMap = MediaStore.Images.Media.getBitmap(
                                getBaseContext().getContentResolver(),
                                result.getData().getData()
                        );

                }else{
                    ImageDecoder.Source source = ImageDecoder.createSource(
                            getContentResolver(),
                            result.getData().getData()
                    );
                    imageBitMap = ImageDecoder.decodeBitmap(source);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imageBitMap != null) {
                //ImageUtil.INSTANCE.imageClicle(imageBitMap, mBinding.imageIcon);
                mViewModel.image.set(imageBitMap);
            }
        }

    });

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
        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_ADD_HABIT, boolean.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowDialog = !Boolean.TRUE.equals(Globals.sharedInstance().get(Globals.c.SHOW_DIALOG_ADD_HABIT, boolean.class));
        if(isShowDialog) {
            DialogUtils.showDialog(this, "Aqui você pode preencher o nome e a descrição do hábito, e adicionar mais variáveis clicando no \"+\". Para remover, basta apertar no ícone de lixeira ao lado do item.\nOpcionalmente, você pode marcar os dias desse hábito. Assim, no dia marcado, o hábito aparecerá na lista de 'hoje'.");
            Globals.sharedInstance().set(Globals.c.SHOW_DIALOG_ADD_HABIT, true);
        }
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

    public void saved() {
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

    private void subscribeSaved() {
        mViewModel.getSavedEvent().observe(this, aVoid -> saved());
    }
    private void subscribeAddVariavel() {
        mViewModel.getVariaveisAdd().observe(this, aVoid -> variavelDialogAdd());
    }

    private void subscribeCarregaVariavel() {
        mViewModel.getVariavelCarrega().observe(this, aVoid -> carregaVariavei());
    }

    private void subscribeCheck() {
        mBinding.labelD.setOnClickListener((view) ->{
            mBinding.checkboxD.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelQ1.setOnClickListener((view) ->{
            mBinding.checkboxQ1.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelQ2.setOnClickListener((view) ->{
            mBinding.checkboxQ2.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelS.setOnClickListener((view) ->{
            mBinding.checkboxS.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelS2.setOnClickListener((view) ->{
            mBinding.checkboxS2.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelS3.setOnClickListener((view) ->{
            mBinding.checkboxS3.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.labelT.setOnClickListener((view) ->{
            mBinding.checkboxT.setChecked(!mBinding.checkboxD.isChecked());
            checkAll();
        });
        mBinding.checkboxD.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.SUNDAY,check);
        });
        mBinding.checkboxQ1.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.WEDNESDAY,check);
        });
        mBinding.checkboxQ2.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.THURSDAY,check);
        });
        mBinding.checkboxS.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.MONDAY,check);
        });
        mBinding.checkboxS2.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.FRIDAY,check);
        });
        mBinding.checkboxS3.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.SATURDAY,check);
        });
        mBinding.checkboxT.setOnCheckedChangeListener((view,check) -> {
            checkAll();
            mViewModel.setDayOfWeek(DayOfWeek.TUESDAY,check);
        });
        mBinding.allDay.setOnClickListener((view) -> {
            boolean check = mBinding.allDay.isChecked();
            mBinding.checkboxD.setChecked(check);
            mBinding.checkboxQ1.setChecked(check);
            mBinding.checkboxQ2.setChecked(check);
            mBinding.checkboxS.setChecked(check);
            mBinding.checkboxS2.setChecked(check);
            mBinding.checkboxS3.setChecked(check);
            mBinding.checkboxT.setChecked(check);
        });
        setCheck();
    }

    private void setCheck(){
        mBinding.checkboxD.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.SUNDAY));
        mBinding.checkboxQ1.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.WEDNESDAY));
        mBinding.checkboxQ2.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.THURSDAY));
        mBinding.checkboxS.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.MONDAY));
        mBinding.checkboxS2.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.FRIDAY));
        mBinding.checkboxS3.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.SATURDAY));
        mBinding.checkboxT.setChecked(mViewModel.containsDayOfWeek(DayOfWeek.TUESDAY));

    }

    private void checkAll(){
        mBinding.allDay.setChecked(
                mBinding.checkboxD.isChecked()
                        && mBinding.checkboxQ1.isChecked()
                        && mBinding.checkboxQ2.isChecked()
                        && mBinding.checkboxS.isChecked()
                        && mBinding.checkboxS2.isChecked()
                        && mBinding.checkboxS3.isChecked()
                        && mBinding.checkboxT.isChecked() );
    }


    private void subscribeImage() {
        mViewModel.image.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Bitmap image = mViewModel.image.get();
                if (image != null) {
                    ImageUtil.INSTANCE.imageClicle(image, mBinding.imageIcon);
                    mBinding.imageIconClose.setVisibility(View.VISIBLE);
                }else {
                    mBinding.imageIcon.setImageDrawable(getDrawable(R.mipmap.ic_habit_defult));
                    mBinding.imageIconClose.setVisibility(View.GONE);
                }
            }
        });

        mBinding.imageIconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.image.set(null);
            }
        });
    }

    private void subscribeButtonImage() {
        mBinding.imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionGaleria();
            }
        });
    }

    private void subscribeDeletVarivaelAntiga() {
        mViewModel.getDeletVariavelAntiga().observe(this, aVoid -> mAdapter.removeItem(mViewModel.posisaoAdapt));
    }

    private void subscribeFalha(){
        mViewModel.getFalha().observe(this, aVoid ->falha());
    }

    private Boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermissionGaleria(){
        boolean permissionGaleriaAceite = checkPermission(PERMISSION_GALERIA);
        if(permissionGaleriaAceite){
            resultGaleria.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }else if(this.shouldShowRequestPermissionRationale(PERMISSION_GALERIA)) {
            showDialogPermission();
        }else {
            //ActivityCompat.requestPermissions(this,new String[]{PERMISSION_GALERIA},100);
            requestGaleria.launch(PERMISSION_GALERIA);
        }
    }

    private void showDialogPermission(){
        DialogUtils.showDialogCallback(
                this,
                R.string.dialog_permission,
                R.string.dialog_alert,
                new DialogUtils.Listener() {
                    @Override
                    public void callBack() {
                        Intent intent = new Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package",getPackageName(),null)
                        );
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(DialogAddHabitItemActivity.REQUEST_SELECT_CAMINHO==requestCode){
            mAdapter.addItem(mViewModel.handleEditRequisitanteResult(resultCode,data));
        }

    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(PERMISSION_GALERIA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        resultGaleria.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                    }else {
                        showDialogPermission();
                    }
                }
            }
        }
    }*/

    private void carregaVariavei(){
        mAdapter.addItems(0,mViewModel.getVariaveis());
        setCheck();
    }

    private void subscribeViewChanges() {
        subscribeSaved();
        subscribeErrorMessage();
        subscribeAddVariavel();
        subscribeCarregaVariavel();
        subscribeCheck();
        subscribeImage();
        subscribeButtonImage();
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
        builder.setPositiveButton(R.string.dialog_button_ok, (dialogInterface, i) -> {
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
