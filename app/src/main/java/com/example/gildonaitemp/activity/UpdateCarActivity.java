package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableRequest;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.api.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateCarActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_update);
        etCarModel = findViewById(R.id.etCarModel);
        etCarNum = (EditText) findViewById(R.id.etCarNum);
        setCarInfo();
        enterCarInfo();
    }
    AutoCompleteTextView etCarModel;
    EditText etCarNum;

    private void setCarInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ConsumableResponse>> call = apiService.getConsumablesByUser(userId);
        call.enqueue(new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    etCarNum.setText(consumables.get(0).getCarNumber());
                    etCarModel.setText(consumables.get(0).getCarModel());
                } else {
                    Log.w("fetchConsumables", "소모품 내역 조회 실패");
                    Toast.makeText(UpdateCarActivity.this, "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(UpdateCarActivity.this, "사용자 없음(에러 코드 : " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateCarActivity.this, "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(UpdateCarActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void enterCarInfo() {

        ArrayList<String> carModelList = new ArrayList<>();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CarModelResponse>> call = apiService.getAllCarModels();
        call.enqueue(new ResponseCallback<List<CarModelResponse>>() {
            public void onSuccess(List<CarModelResponse> carModels) {
                for(CarModelResponse model : carModels) {
                    Log.e("carModel", model.getModelName());
                    carModelList.add(model.getModelName());
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                carModelList
        );
        etCarModel.setAdapter(adapter);
        etCarModel.setThreshold(1); //첫글자가 일치해야 검색가능

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

        ConsumableRequest request = new ConsumableRequest(userId, carModel, carNumber);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumableResponse> call = apiService.createConsumable(request);

        call.enqueue(new ResponseCallback<ConsumableResponse>() {
            @Override
            public void onSuccess(ConsumableResponse response) {
                Log.d("UpdateCar", "차량수정");
                Toast.makeText(UpdateCarActivity.this, "차량 수정 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UpdateCarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<ConsumableResponse> response) {
                super.onError(response);
                Toast.makeText(UpdateCarActivity.this, "차량수정에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
