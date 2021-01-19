package com.example.myapplication.common.lifecycle;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LifecycleOwner;

public class SimpleDialogMessage extends SingleLiveEvent<Integer>{

    @MainThread
    public void observeMessage(@NonNull LifecycleOwner owner, @NonNull MessageObserver observer) {
        super.observe(owner, messageResId -> {
            if(messageResId == null){
                return;
            }
            observer.onNewMessage(messageResId);
        });
    }

    public interface MessageObserver{
        /**
         * Called when there is a new body to be shown.
         * @param messageResId The new body, non-null.
         */
        void onNewMessage(@StringRes int messageResId);
    }
}
