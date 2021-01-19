package com.example.myapplication.common.lifecycle;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;

/**
 * A SingleLiveEvent used for Snackbar messages. Like a {@link SingleLiveEvent} but also prevents
 *
 */
public class SnackbarMessage extends SingleLiveEvent<Integer>{

    @MainThread
    public void observeMessage(@NonNull LifecycleOwner owner, @NonNull SnackbarObserver observer) {
        super.observe(owner, messageResId -> {
            if(messageResId == null){
                return;
            }
            observer.onNewMessage(messageResId);
        });
    }

    public interface SnackbarObserver{
        /**
         * Called when there is a new body to be shown.
         * @param snackbarMessageResId The new body, non-null.
         */
        void onNewMessage(@StringRes int snackbarMessageResId);
    }
}
