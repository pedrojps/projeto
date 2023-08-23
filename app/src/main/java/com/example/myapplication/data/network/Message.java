package com.example.myapplication.data.network;


import androidx.annotation.Nullable;

import com.google.common.base.Strings;

public class Message {

    @Nullable
    public final String header;

    @Nullable
    public final String body;

    private Message(@Nullable String header, @Nullable String message) {
        this.header = header;
        this.body = message;
    }

    private Message(@Nullable String message) {
        this("", message);
    }

    public static Message empty(){
        return new Message("");
    }

    public static Message error(String message){
        return new Message("Error", message);
    }

}
