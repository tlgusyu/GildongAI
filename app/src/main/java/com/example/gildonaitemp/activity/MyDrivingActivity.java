package com.example.gildonaitemp.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.dto.DrivingPatternResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.dto.WeeklyDrivingPatterns;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyDrivingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_driving);
        getDrivingHistory();
    }

    private void getDrivingHistory() {
        TextView drivingHistory = (TextView) findViewById(R.id.driving_history);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<WeeklyDrivingPatterns>> call = apiService.getWeeklyDrivingPatterns(userId);

        call.enqueue(new ResponseCallback<List<WeeklyDrivingPatterns>>() {
            @Override
            public void onSuccess(List<WeeklyDrivingPatterns> patterns) {
                for (WeeklyDrivingPatterns pattern : patterns) {
                    String addHistory = pattern.getWeekStart().substring(0, 10)
                            + "부터 일주일 동안의 평균점수는 "
                            + pattern.getAverageScore() + "점 입니다.\n"
                            + drivingHistory.getText().toString();
                    drivingHistory.setText(addHistory);
                }
            }

            @Override
            public void onError(Response<List<WeeklyDrivingPatterns>> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(MyDrivingActivity.this, "사용자 없음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyDrivingActivity.this, "운전 패턴 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}