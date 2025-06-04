package com.example.gildonaitemp.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.widget.Toast;

import com.example.gildonaitemp.activity.LoginActivity;

import java.io.IOException;

public abstract class ResponseCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            onError(response);
        }
    }

    public abstract void onSuccess(T body);

    public void onError(Response<T> response) { }

    @Override
    public void onFailure(Call<T> call, Throwable t) { }
}
