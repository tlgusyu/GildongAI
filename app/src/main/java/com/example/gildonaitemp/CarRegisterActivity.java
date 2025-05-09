package com.example.gildonaitemp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CarRegisterActivity extends AppCompatActivity {

    private EditText etModelName, etCarNumber;
    private Button btnRegisterCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        etModelName = findViewById(R.id.etModelName);
        etCarNumber = findViewById(R.id.etCarNumber);
        btnRegisterCar = findViewById(R.id.btnRegisterCar);

        btnRegisterCar.setOnClickListener(v -> {
            String modelName = etModelName.getText().toString().trim();
            String carNumber = etCarNumber.getText().toString().trim();

            if (modelName.isEmpty() || carNumber.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: 백엔드 연동 후 차량 등록 로직 추가
            Toast.makeText(this, "차량이 등록되었습니다!", Toast.LENGTH_SHORT).show();
            finish(); // 등록 후 뒤로가기
        });
    }
}
