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
import com.example.myapplication.databinding.DialogSetVariavelBinding;

public class DialogAddHabitItemActivity
        extends AppCompatActivity {

    public static final String NAME_KEY = "NAME_KEY";

    public static final String TIPO_KEY = "TIPO_KEY";

    public static final int REQUEST_SELECT_CAMINHO = 166;

    private DialogSetVariavelBinding mBinding;

    private DialogAddHabitItemViewModel mViewModel;

    public static Intent getNewIntent(@NonNull Context context){
        return new Intent(context, DialogAddHabitItemActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();

        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_set_variavel);
        mBinding.setVm(mViewModel);

        subscribeCancel();
        subscribeSave();

        mViewModel.nameVarialvel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String valido = mViewModel.nameVarialvel.get();
                if(valido!=null && !valido.isEmpty())
                    mViewModel.salva.set(mViewModel.getTipo()!=-1&&true);
                else
                    mViewModel.salva.set(false);
            }
        });
    }

    private void subscribeSave(){
        mViewModel.getHostSaved().observe(this, this::hostSaved);
    }

    private void subscribeCancel(){
        mViewModel.getCanceled().observe(this, aVoid -> canceled());
    }

    private DialogAddHabitItemViewModel findOrCreateViewModel(){

        DialogAddHabitItemViewModel.Factory factory = new DialogAddHabitItemViewModel.Factory(
                getApplication()
        );

        return ViewModelProviders.of(this, factory).get(DialogAddHabitItemViewModel.class);
    }

    public void hostSaved(String name) {
        Intent it = new Intent();
        it.putExtra(NAME_KEY, name);
        it.putExtra(TIPO_KEY, mViewModel.getTipo()+"");
        setResult(RESULT_OK, it);
        finish();
    }

    public void canceled() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
