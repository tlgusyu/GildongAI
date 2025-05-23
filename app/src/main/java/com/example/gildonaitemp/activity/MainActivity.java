package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.ServerUserResponse;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMainPage();
        setClickEvent();
    }

    private void setMainPage() {
        TextView driverNameTextView = (TextView) findViewById(R.id.driver_name);
        TextView driverScoreTextView = (TextView) findViewById(R.id.driver_score);
        CircularProgressIndicator driverScoreProgress = (CircularProgressIndicator) findViewById(R.id.driver_score_bar);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ServerUserResponse> call = apiService.getUserById(userId);

        call.enqueue(new ResponseCallback<ServerUserResponse>() {
            @Override
            public void onSuccess(ServerUserResponse user) {
                driverNameTextView.setText(user.getUserName());

                int drivingScore = user.getAvgDrivingScore();
                driverScoreTextView.setText(String.valueOf(drivingScore));
                driverScoreProgress.setProgress(drivingScore);
            }

            @Override
            public void onError(Response<ServerUserResponse> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Log.e("fetchUserById", "사용자 없음(에러 코드 : " + response.code());
                } else {
                    Log.e("fetchUserById", "사용자 정보 조회 실패");
                }
            }

            @Override
            public void onFailure(Call<ServerUserResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.e("fetchUserById", "네트워크 오류", t);
            }
        });


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
        Button submenu3 = (Button) findViewById(R.id.btn_check_camera);
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

        Button logoutButton = (Button) findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

    }

}