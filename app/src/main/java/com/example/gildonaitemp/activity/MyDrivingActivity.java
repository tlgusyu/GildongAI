package com.example.gildonaitemp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.dto.WeeklyDrivingPatternsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyDrivingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_driving);

        getDrivingHistory();
    }

    private void getDrivingHistory() {
        TextView drivingHistory = (TextView) findViewById(R.id.driving_history);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getWeeklyDrivingPatterns(userId, new ResponseCallback<List<WeeklyDrivingPatternsResponse>>() {
            @Override
            public void onSuccess(List<WeeklyDrivingPatternsResponse> patterns) {
                for (WeeklyDrivingPatternsResponse pattern : patterns) {
                    String addHistory = pattern.getWeekStart().substring(0, 10)
                            + "부터 일주일 동안의 평균점수는 "
                            + String.format("%.1f", pattern.getAverageScore()) + "점 입니다.\n"
                            + drivingHistory.getText().toString();
                    drivingHistory.setText(addHistory);
                }
            }

            @Override
            public void onError(Response<List<WeeklyDrivingPatternsResponse>> response) {
                if (response.code() == 404) {
                    Toast.makeText(MyDrivingActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyDrivingActivity.this, "운전 점수 기록 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WeeklyDrivingPatternsResponse>> call, Throwable t) {
                Toast.makeText(MyDrivingActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }
}