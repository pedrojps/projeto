package com.example.myapplication.data.network;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Resource<T> {

    @SerializedName("State")
    @NonNull
    public final Status status;

    @SerializedName("Data")
    @Nullable
    public final T data;

    @SerializedName("Message")
    @NonNull
    public final Message message;

    private Resource(@NonNull Status status, @Nullable T data, @NonNull Message message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data){
        return new Resource<>(Status.SUCCESS, data, Message.empty());
    }

    public static <T> Resource<T> error(Throwable throwable){
        return new Resource<>(Status.ERROR, null, Message.error(throwable.getMessage()));
    }

    public static <T> Resource<T> loading(@Nullable T data){
        return new Resource<>(Status.LOADING, data, Message.empty());
    }

}
