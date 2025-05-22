package com.example.gildonaitemp.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public abstract class ResponseCallback<T> implements Callback<T> {

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e("API_ERROR", "통신 실패", t);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            onError(response);
        }
    }

    public abstract void onSuccess(T body);


    public void onError(Response<T> response) {
        Log.e("API_ERROR", "에러 코드 " + response.code() + " : " + response.message());
    }
}
