package com.example.myapplication.common.lifecycle;

import android.util.Pair;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

/**
 * A SingleLiveEvent used for AlertDialog error messages. Like a {@link SnackbarMessage} prevents
 * null body and uses a custom observer.
 */
public class ErrorDialogMessage extends SingleLiveEvent<Pair<String, String>>{

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull ErrorDialogObserver observer) {
        super.observe(owner, errorMessagePair -> {
            if(errorMessagePair == null){
                return;
            }

            observer.onNewErrorMessage(errorMessagePair.first, errorMessagePair.second);
        });
    }

    @MainThread
    public void setValue(String header, String message) {
        super.setValue(new Pair<>(header, message));
    }

    public interface ErrorDialogObserver{

        void onNewErrorMessage(String header, String message);
    }
}
