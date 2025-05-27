package com.example.gildonaitemp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //private static final String BASE_URL = "http://10.0.2.2:8080/"; //애뮬레이터
    // network_security_config.xml <domain>10.0.2.2</domain>
    private static final String BASE_URL = "http://43.201.30.198:8080/";
    //spring.data.mongodb.uri=mongodb+srv://metamorph:apxkahfvm7336@gildongedb.moa3m6x.mongodb.net/gildongE_db?retryWrites=true&w=majority&appName=GildongEDB
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
