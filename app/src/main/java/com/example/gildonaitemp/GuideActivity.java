package com.example.gildonaitemp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    ArrayList<Button> tipList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        setTips();
    }

    private void setTips() {
        RecyclerView searchedTips = (RecyclerView) findViewById(R.id.driveTips);

        String[] buttonTexts = {"접촉 사고 발생 시 대처 요령", "주행 중 시동이 꺼졌을 때 대처 요령",
                "급발진 대처 요령", "교차로 우회전 방법", "비보호 좌회전 방법",
                "올바른 유턴 방법", "원형 교차로(회전교차로)에서의 통행 방법"};

        // 버튼 만들어서 searchedTips에 추가
    }
}