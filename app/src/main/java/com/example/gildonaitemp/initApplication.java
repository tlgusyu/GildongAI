package com.example.gildonaitemp;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class initApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "092e3b2df3d1065f7f68be2254ba62f9");
    }
}
