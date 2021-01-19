package com.example.myapplication.data.network;

import com.google.gson.Gson;

public class IdentifiedRequest<TCredentials, TData> implements IIdentifiedRequest<TCredentials, TData> {

    private TCredentials credentials;

    private TData data;

    public IdentifiedRequest() {

    }

    public IdentifiedRequest(TCredentials credentials, TData data) {
        this.credentials = credentials;
        this.data = data;
    }

    @Override
    public TCredentials getCredentials() {
        return this.credentials;
    }

    @Override
    public void setCredentials(TCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public TData getData(){
        return this.data;
    }

    @Override
    public void setData(TData data){
        this.data = data;
    }

    @Override
    public String stringify(Gson gson){
        return gson.toJson(this);
    }

}
