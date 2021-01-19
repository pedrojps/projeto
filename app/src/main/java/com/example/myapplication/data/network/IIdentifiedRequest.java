package com.example.myapplication.data.network;

import com.google.gson.Gson;

public interface IIdentifiedRequest<TCredentials, TData> {

    TCredentials getCredentials();

    void setCredentials(TCredentials credentials);

    TData getData();

    void setData(TData data);

    String stringify(Gson gson);

}
