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

    public final ObservableField<String> requisitante = new ObservableField<>("");

    public final ObservableField<Integer> tipo = new ObservableField<>(-1);

    public final ObservableBoolean pernitiOk = new ObservableBoolean(false);

    private final SingleLiveEvent<String> mRequisitante = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mCanceled = new SingleLiveEvent<>();

    public DialogViewModel(
            @NonNull Application application//,
            //@NonNull String requisitante
    ) {
        super(application);
       // setRequisitante(requisitante);
    }

    private void setRequisitante(String requisitante) {
        this.requisitante.set(requisitante);
    }

//    public void start(String serverHost, int serverPort, boolean isDefaultPort) {
//        estabelecimento.set(serverHost);
//        port.set(serverPort);
//        useDefaultPort.set(isDefaultPort);
//    }

    public void save() {
        String requisitante = this.requisitante.get();
        mRequisitante.setValue(requisitante);
    }

    public void cancel() {
        mCanceled.call();
    }

    public SingleLiveEvent<String> getHostSaved() {
        return mRequisitante;
    }

    public SingleLiveEvent<Void> getCanceled() {
        return mCanceled;
    }

    public Integer getTipo() {
        return tipo.get();
    }
    public void setTipo(int tipo) {
        if(tipo!=this.tipo.get()){
            this.tipo.set(tipo);
            pernitiOk.set(true&&requisitante.get().length()!=0);
        }
    }
    public static class Factory implements ViewModelProvider.Factory {

        private final Application mApplication;

       // private final String mRequisitante;

        public Factory(Application application/*, String requisitante*/) {
            mApplication = application;
            //mRequisitante = requisitante;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DialogViewModel(mApplication/*, mRequisitante*/);
        }
    }
}
