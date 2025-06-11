package com.example.gildonaitemp.activity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.application.initApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userId == null) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    ((initApplication)getApplicationContext()).initializeSSEConnection(userId);

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 3000);

    }
}