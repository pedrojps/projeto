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

    public static Message make(String header, String message){
        return new Message(header, message);
    }

    public static Message error(String message){
        return new Message("Error", message);
    }

    public static Message make(String message){
        if (Strings.isNullOrEmpty(message)){
            return empty();
        }

        String t, m;

        t = splitServerMessageTitle(message);
        m = splitServerMessageDetail(message);

        return Strings.isNullOrEmpty(m) ? new Message("Erro na validação de dados", t) : new Message(t, m);
    }

    private static String splitServerMessageDetail(String message) {
        if(!Strings.isNullOrEmpty(message) && message.contains("Type=EExpected")){
            String messageChunks[] = message.split("[|]");
            for (String chunk : messageChunks) {
                String[] stripes = chunk.split("[=]");
                if(stripes[0].equals("Detail")){
                    return (stripes.length <= 1 ? "" : stripes[1]);
                }
            }
        }
        return message;
    }

    private static String splitServerMessageTitle(String message){
        if(!Strings.isNullOrEmpty(message) && message.contains("Type=EExpected")){
            String messageChunks[] = message.split("[|]");
            for (String chunk : messageChunks) {
                String[] stripes = chunk.split("[=]");
                if(stripes[0].equals("Title")){
                    return (stripes.length <= 1 ? "" : stripes[1]);
                }
            }
        }
        return "";
    }
}
