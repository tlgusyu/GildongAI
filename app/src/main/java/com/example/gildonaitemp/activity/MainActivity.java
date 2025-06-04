package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.UserResponse;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(android.R.id.content).setAlpha(0f);

        Button logoutButton = (Button) findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        isCarRegistered();
    }

    private void isCarRegistered() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getConsumablesByUser(userId, new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables == null || consumables.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, RegisterCarActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    setMainPage();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                findViewById(android.R.id.content).setAlpha(1f);if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "차량 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                findViewById(android.R.id.content).setAlpha(1f);
                Toast.makeText(MainActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMainPage() {
        findViewById(android.R.id.content).setAlpha(1f);

        TextView driverNameTextView = (TextView) findViewById(R.id.driver_name);
        TextView driverScoreTextView = (TextView) findViewById(R.id.driver_score);
        CircularProgressIndicator driverScoreProgress = (CircularProgressIndicator) findViewById(R.id.driver_score_bar);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getUserById(userId, new ResponseCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse user) {
                driverNameTextView.setText(user.getUserName());
                double drivingScore = user.getAvgDrivingScore();
                driverScoreTextView.setText(String.valueOf((int) drivingScore));
                driverScoreProgress.setProgress((int) drivingScore);
            }

            @Override
            public void onError(Response<UserResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "사용자 정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        setClickEvent();

    }

    private void setClickEvent() {
        Button submenu1 = (Button) findViewById(R.id.btn_manage_profile);
        submenu1.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageAccountActivity.class);
            startActivity(intent);
        });

        Button submenu2 = (Button) findViewById(R.id.btn_manage_vehicle);
        submenu2.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageCarActivity.class);
            startActivity(intent);
        });
        Button submenu3 = (Button) findViewById(R.id.btn_drive_history);
        submenu3.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyDrivingActivity.class);
            startActivity(intent);
        });
        Button submenu4 = (Button) findViewById(R.id.btn_emergency_guide);
        submenu4.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
        });

        ImageView alarmButton = findViewById(R.id.AlarmButton);
        alarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

    }

}