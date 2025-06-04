package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.ConsumableCarUpdateRequest;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterCarActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        enterCarInfo();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                prefs.edit().clear().apply();

                Intent intent = new Intent(RegisterCarActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void enterCarInfo() {
        AutoCompleteTextView etCarModel = findViewById(R.id.etCarModel);
        EditText etCarNum = (EditText) findViewById(R.id.etCarNum);

        ArrayList<String> carModelList = new ArrayList<>();

        ApiCaller.getAllCarModels(new ResponseCallback<List<CarModelResponse>>() {
            @Override
            public void onSuccess(List<CarModelResponse> carModels) {
                for (CarModelResponse model : carModels) {
                    carModelList.add(model.getModelName());
                }
            }

            @Override
            public void onError(Response<List<CarModelResponse>> response) {
                Toast.makeText(RegisterCarActivity.this, "차량 모델을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<CarModelResponse>> call, Throwable t) {
                Toast.makeText(RegisterCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                carModelList
        );
        etCarModel.setAdapter(adapter);
        etCarModel.setThreshold(0);
        etCarModel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                etCarModel.showDropDown();
            }
        });
        etCarModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    etCarModel.requestFocus();
                    etCarModel.postDelayed(() -> etCarModel.showDropDown(), 100);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        Button addCarBtn = (Button) findViewById(R.id.addCarBtn);
        addCarBtn.setOnClickListener(v -> {
            String carModel = etCarModel.getText().toString();
            String carNumber = etCarNum.getText().toString();
            if (carModel.isEmpty() || carNumber.isEmpty()) {
                Toast.makeText(RegisterCarActivity.this, "차량모델, 차량번호를 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                registerCar(carModel, carNumber);
            }

        });
    }

    private void registerCar(String carModel, String carNumber) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ConsumableCarUpdateRequest request = new ConsumableCarUpdateRequest(userId, carModel, carNumber);
        ApiCaller.createConsumableCar(request, new ResponseCallback<ConsumableResponse>() {
            @Override
            public void onSuccess(ConsumableResponse response) {
                Toast.makeText(RegisterCarActivity.this, "차량 등록 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterCarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<ConsumableResponse> response) {
                if (response.code() == 400) {
                    Toast.makeText(RegisterCarActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(RegisterCarActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterCarActivity.this, "차량등록에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConsumableResponse> call, Throwable t) {
                Toast.makeText(RegisterCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
