package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableCarUpdateRequest;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateCarActivity extends AppCompatActivity {

    AutoCompleteTextView etCarModel;
    EditText etCarNum;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_update);

        etCarModel = findViewById(R.id.etCarModel);
        etCarNum = (EditText) findViewById(R.id.etCarNum);

        setCarInfo();
        enterCarInfo();
    }

    private void setCarInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getConsumablesByUser(userId, new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    etCarNum.setText(consumables.get(0).getCarNumber());
                    etCarModel.setText(consumables.get(0).getCarModel());
                } else {
                    Toast.makeText(UpdateCarActivity.this, "차량 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                if (response.code() == 404) {
                    Toast.makeText(UpdateCarActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateCarActivity.this, "차량 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                Toast.makeText(UpdateCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void enterCarInfo() {
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
                Toast.makeText(UpdateCarActivity.this, "차량 모델을 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<CarModelResponse>> call, Throwable t) {
                Toast.makeText(UpdateCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
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

        Button updateCarBtn = (Button) findViewById(R.id.updateCarBtn);
        updateCarBtn.setOnClickListener(v -> {
            String carModel = etCarModel.getText().toString();
            String carNumber = etCarNum.getText().toString();
            if (carModel.isEmpty() || carNumber.isEmpty()) {
                Toast.makeText(UpdateCarActivity.this, "차량모델, 차량번호를 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                updateCar(carModel, carNumber);
            }

        });
    }

    private void updateCar(String carModel, String carNumber) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ConsumableCarUpdateRequest request = new ConsumableCarUpdateRequest(userId, carModel, carNumber);
        ApiCaller.createConsumableCar(request, new ResponseCallback<ConsumableResponse>() {
            @Override
            public void onSuccess(ConsumableResponse response) {
                Toast.makeText(UpdateCarActivity.this, "차량 수정 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UpdateCarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<ConsumableResponse> response) {
                if (response.code() == 400) {
                    Toast.makeText(UpdateCarActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(UpdateCarActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateCarActivity.this, "차량수정에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConsumableResponse> call, Throwable t) {
                Toast.makeText(UpdateCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
