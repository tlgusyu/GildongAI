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
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.ConsumableRequest;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
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
    }

    private void enterCarInfo() {
        AutoCompleteTextView etCarModel = findViewById(R.id.etCarModel);
        EditText etCarNum = (EditText) findViewById(R.id.etCarNum);

        ArrayList<String> carModelList = new ArrayList<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CarModelResponse>> call = apiService.getAllCarModels();
        call.enqueue(new ResponseCallback<List<CarModelResponse>>() {
            public void onSuccess(List<CarModelResponse> carModels) {
                for(CarModelResponse model : carModels) {
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

        ConsumableRequest request = new ConsumableRequest(userId, carModel, carNumber);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumableResponse> call = apiService.createConsumable(request);

        call.enqueue(new ResponseCallback<ConsumableResponse>() {
            @Override
            public void onSuccess(ConsumableResponse response) {
                Log.d("RegisterCar", "차량등록");
                Toast.makeText(RegisterCarActivity.this, "차량 등록 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterCarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<ConsumableResponse> response) {
                super.onError(response);
                Toast.makeText(RegisterCarActivity.this, "차량등록에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
