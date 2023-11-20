package com.example.myapplication.common.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

public class SingleLiveEvent<T>  extends MutableLiveData<T> {

        private static final String TAG = "SingleLiveEvent";

        private final AtomicBoolean mPending = new AtomicBoolean(false);

        @MainThread
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            if(hasActiveObservers()){
                Timber.w("Multiple observers registered but only one will be notified of changes.");
            }

            super.observe(owner, value -> {
                if(mPending.compareAndSet(true, false)){
                    observer.onChanged(value);
                }
            });
        }

        @MainThread
        @Override
        public void setValue(T value) {
            mPending.set(true);
            super.setValue(value);
        }

        /**
         * Convenience method to call when T is void, make calls cleaner.
         */
        @MainThread
        public void call(){
            setValue(null);
        }
}
