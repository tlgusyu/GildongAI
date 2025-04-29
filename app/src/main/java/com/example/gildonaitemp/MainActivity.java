package com.example.gildonaitemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.CircularProgressIndicator;

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

        //서버에서 사용자 정보 (이름, 운전점수 등) 가져오기
        String driverName = "홍길동";
        String driverScore = "90"; //임시값

        driverNameTextView.setText(driverName);
        driverScoreTextView.setText(driverScore);
        driverScoreProgress.setProgress(Integer.parseInt(driverScore));
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
            Intent intent = new Intent(this, CameraActivity.class);
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