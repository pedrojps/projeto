package com.example.myapplication.ui.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.common.lifecycle.SingleLiveEvent;

public class DialogViewModel extends AndroidViewModel {

    public final ObservableField<String> nameVarialvel = new ObservableField<>("");

    public final ObservableField<Integer> tipo = new ObservableField<>(-1);

    public final ObservableBoolean salva = new ObservableBoolean(false);

    private final SingleLiveEvent<String> mNameVarialvel = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCanceled = new SingleLiveEvent<>();

    public DialogViewModel(
            @NonNull Application application
    ) {
        super(application);
    }

    public void save() {
        String requisitante = this.nameVarialvel.get();
        mNameVarialvel.setValue(requisitante);
    }

    public void cancel() {
        mCanceled.call();
    }

    public SingleLiveEvent<String> getHostSaved() {
        return mNameVarialvel;
    }

    public SingleLiveEvent<Void> getCanceled() {
        return mCanceled;
    }

    public Integer getTipo() {
        return tipo.get();
    }

    public void setTipo(int tipo) {
        this.tipo.set(tipo);
        salva.set(true&& nameVarialvel.get().length()!=0);
    }
    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;


        public Factory(Application application) {
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DialogViewModel(mApplication);
        }
    }
}
