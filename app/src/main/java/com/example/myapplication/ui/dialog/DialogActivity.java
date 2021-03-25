package com.example.myapplication.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.databinding.DialogCaminhoBinding;

public class DialogActivity
        extends AppCompatActivity {

    public static final String CAMINHO_KEY = "CAMINHO_KEY";

    public static final String TIPO_KEY = "TIPO_KEY";

    public static final int REQUEST_SELECT_CAMINHO = 166;

    private DialogCaminhoBinding mBinding;

    private DialogViewModel mViewModel;

    public static Intent getNewIntent(@NonNull Context context/*, String requisitante*/){
        return new Intent(context, DialogActivity.class);
                //.putExtra(CAMINHO_KEY, requisitante);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_caminho);
        mBinding.setVm(mViewModel);

        subscribeCancel();
        subscribeSave();

        mViewModel.requisitante.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String valido = mViewModel.requisitante.get();
                if(valido!=null && !valido.isEmpty())
                    mViewModel.pernitiOk.set(mViewModel.getTipo()!=-1&&true);
                else
                    mViewModel.pernitiOk.set(false);
            }
        });
    }

    private void subscribeSave(){
        mViewModel.getHostSaved().observe(this, this::hostSaved);
    }

    private void subscribeCancel(){
        mViewModel.getCanceled().observe(this, aVoid -> canceled());
    }

    private DialogViewModel findOrCreateViewModel(){
       // String requsisitante = getIntent().getStringExtra(CAMINHO_KEY);

        DialogViewModel.Factory factory = new DialogViewModel.Factory(
                getApplication()
        );

        return ViewModelProviders.of(this, factory).get(DialogViewModel.class);
    }

    public void hostSaved(String hostAndPort) {
        Intent it = new Intent();
        it.putExtra(CAMINHO_KEY, hostAndPort);
        it.putExtra(TIPO_KEY, mViewModel.getTipo()+"");
        setResult(RESULT_OK, it);
        finish();
    }

    public void canceled() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
