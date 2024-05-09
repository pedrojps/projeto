package com.example.myapplication.ui.select_image;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogSelectImageBinding;
import com.example.myapplication.utils.DialogUtils;
import com.example.myapplication.utils.ImageUtil;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectImageActivity extends AppCompatActivity {

    public static final int REQUEST_SELECT_CAMINHO = 176;

    private DialogSelectImageBinding mBinding;

    private Bitmap imageSelect = null;

    private static final String PERMISSION_GALERIA = ImageUtil.INSTANCE.permissionReadImage();

    private SelectImageAdapter mAdapter;

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
                ImageUtil.INSTANCE.imageClicle(imageBitMap, mBinding.imageIcon);
                imageSelect = (imageBitMap);
            }
        }

    });

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

    private Boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
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

    public static Intent getNewIntent(@NonNull Context context){
        return new Intent(context, SelectImageActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_select_image);
        ArrayList<Integer> itens = getItens();
        mAdapter = new SelectImageAdapter(itens);

        mAdapter.setListener((position, item) ->  {
            Drawable image = getDrawable(item);
            imageSelect = ImageUtil.INSTANCE.imageClicle(image, mBinding.imageIcon);
        });

        mBinding.iconList.setAdapter(mAdapter);
        mBinding.imageSelect.setOnClickListener((v -> {
            checkPermissionGaleria();
        }));

        mBinding.btnDialogCancel.setOnClickListener((v -> {
            canceled();
        }));
        mBinding.btnDialogSave.setOnClickListener((v -> {
            saved();
        }));

        mBinding.iconList.setLayoutManager(new GridLayoutManager(this, 4));
    }

    private ArrayList<Integer> getItens(){
        ArrayList<Integer> itens = new ArrayList<>();

        itens.add(R.drawable.ic_baseline_sports_kabaddi_24);

        return itens;
    }

    public void saved() {
        Intent it = new Intent();
        ImageUtil.INSTANCE.saveImage(this,imageSelect,"temp");
        setResult(RESULT_OK, it);
        finish();
    }

    public void canceled() {
        setResult(RESULT_CANCELED);
        finish();
    }
}