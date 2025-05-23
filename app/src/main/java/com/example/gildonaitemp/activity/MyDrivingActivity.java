package com.example.gildonaitemp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.DrivingPatternResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.api.ResponseCallback;

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
        Call<List<DrivingPatternResponse>> call = apiService.getDrivingPatterns(userId);

        call.enqueue(new ResponseCallback<List<DrivingPatternResponse>>() {
            @Override
            public void onSuccess(List<DrivingPatternResponse> patterns) {
                for (DrivingPatternResponse pattern : patterns) {
                    String addHistory = drivingHistory.getText().toString() +
                            pattern.getRecordedAt() + "\t" + pattern.getDrivingScore() + "\n";
                    drivingHistory.setText(addHistory);
                }
            }

            @Override
            public void onError(Response<List<DrivingPatternResponse>> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(MyDrivingActivity.this, "사용자 조회 실패", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyDrivingActivity.this, "운전 패턴 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}