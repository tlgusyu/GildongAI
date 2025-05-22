package com.example.gildonaitemp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getAccuracy();
        getCamera();
    }

    private void getAccuracy() {
        // 임시
        int accuracy = 90;

        TextView accuracyText = (TextView) findViewById(R.id.accuracy);
        TextView alertText = (TextView) findViewById(R.id.alert);
        accuracyText.setText(String.valueOf(accuracy) + "%");
        if (accuracy < 35) {
            accuracyText.setTextColor(Color.RED); //임시 색
            alertText.setText("카메라 위치를 조정해주세요");
        }
        else if (accuracy < 70) {
            accuracyText.setTextColor(Color.YELLOW);
            alertText.setText("카메라 위치를 조정해주세요");
        }
        else {
            accuracyText.setTextColor(Color.GREEN);
            alertText.setText("카메라 위치가 좋습니다");
        }
    }

    private void getCamera() {
        // 영상 서버에서 가져오기
    }
}