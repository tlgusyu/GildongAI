package com.example.gildonaitemp.activity;
import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableOverviewResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.api.ResponseCallback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class ManageCarActivity extends AppCompatActivity {

    ArrayList<Button> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);

        setNextDue();
        //setCarInfo();
    }

    private void setNextDue() {
        TextView schedule = (TextView) findViewById(R.id.schedule);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumableOverviewResponse> call = apiService.getConsumableOverview(userId);

        call.enqueue(new ResponseCallback<ConsumableOverviewResponse>() {
            @Override
            public void onSuccess(ConsumableOverviewResponse consumableOverview) {
                String nextDueDate = consumableOverview.getNextDueDate();
                if (nextDueDate != null) {
                    String nextDueDateFormat = nextDueDate.substring(0, 4) + "-" +
                            nextDueDate.substring(4, 6) + "-" +
                            nextDueDate.substring(6, 8);
                    Log.d("NEXT_DUE", "다음 교체 예정일: " + nextDueDateFormat);
                    schedule.setText(nextDueDateFormat);
                } else {
                    Log.d("NEXT_DUE", "예정된 교체 항목이 없습니다.");
                    Toast.makeText(ManageCarActivity.this, "정비 예정일이 없습니다", Toast.LENGTH_SHORT).show();
                }

                setupUIWithConsumable(consumableOverview.getAll().get(0));
            }

            @Override
            public void onError(Response<ConsumableOverviewResponse> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "사용자 조회 실패" + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "ConsumableOverview 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    private void setCarInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ConsumableResponse>> call = apiService.getConsumablesByUser(userId);

        call.enqueue(new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    setupUIWithConsumable(consumables.get(0));
                } else {
                    Log.w("fetchConsumables", "소모품 내역 조회 실패");
                    Toast.makeText(getApplicationContext(), "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(getApplicationContext(), "사용자 없음(에러 코드 : " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getApplicationContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

    }
*/
    private void setupUIWithConsumable(ConsumableResponse data) {

        String carModel = data.getCarModel();

        TextView carNumberTextView = (TextView) findViewById(R.id.carNumber);
        TextView carModelTextView = (TextView) findViewById(R.id.carModel);
        carNumberTextView.setText(data.getCarNumber());
        carModelTextView.setText(carModel);

        LinearLayout linearMenu = (LinearLayout) findViewById(R.id.menu);
        linearMenu.setOrientation(LinearLayout.VERTICAL);

        String[] buttonTexts = {
                "엔진 오일 관리", "배터리 관리",
                "기타 소모품 관리", "정비 내역 조회"
        };

        Map<String, String> descriptionMap = new HashMap<>();
        descriptionMap.put("엔진 오일 관리", "엔진 오일 교체 예정일 : " + safeString(data.getEngineOilDate()));
        descriptionMap.put("배터리 관리", "배터리 교체 예정일 : " + safeString(data.getBatteryDate()));
        descriptionMap.put("기타 소모품 관리",
                "부동액 교체 예정일 : " + safeString(data.getCoolantDate()) + "\n" +
                "변속기 오일 교체 예정일 : " + safeString(data.getTransmissionOilDate()) + "\n" +
                "브레이크 오일 교체 예정일 : " + safeString(data.getBrakeOilDate()) + "\n" +
                "워셔 액 교체 예정일 : " + safeString(data.getAirconFilterDate())
        );
        descriptionMap.put("정비 내역 조회",
                "엔진 오일 교체 완료일 : " + safeString(data.getEngineOilChangedDate()) + "\n" +
                "배터리 교체 완료일 : " + safeString(data.getBatteryChangedDate()) + "\n" +
                "부동액 교체 완료일 : " + safeString(data.getCoolantChangedDate()) + "\n" +
                "변속기 오일 교체 완료일 : " + safeString(data.getTransmissionOilChangedDate()) + "\n" +
                "브레이크 오일 교체 완료일 : " + safeString(data.getBrakeOilChangedDate()) + "\n" +
                "워셔 액 교체 완료일 : " + safeString(data.getAirconFilterChangedDate())
        );

        SetManualBtn(carModel);

        for (String text : buttonTexts) {
            addMenuButton(text, descriptionMap);
        }
    }

    private void SetManualBtn(String carModel) {
        Button carManual = (Button) findViewById(R.id.manual);
        SpannableStringBuilder carModelBoldBuilder = new SpannableStringBuilder();
        SpannableString carModelBold = new SpannableString(carModel);
        carModelBold.setSpan(new StyleSpan(Typeface.BOLD), 0, carModel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        carModelBoldBuilder.append(carModelBold);
        carModelBoldBuilder.append(" 차량 메뉴얼 보기");
        carManual.setText(carModelBoldBuilder);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CarModelResponse>> call = apiService.getCarModelsByModelName(carModel);
        call.enqueue(new ResponseCallback<List<CarModelResponse>>() {
            @Override
            public void onSuccess(List<CarModelResponse> carModels) {
                for (CarModelResponse car : carModels) {
                    Log.d("CAR_MODEL", car.getModelName() + car.getManualUrl());
                    carManual.setOnClickListener(v -> {
                        String url = car.getManualUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    });

                }
            }
        });
    }

    String safeString(String s) {
        return s == null ? "" : s;
    }

    private Button addMenuButton(String buttonText, Map descriptionMap) {

        Button menu = new Button(this);
        menu.setBackgroundResource(R.drawable.menu_button);
        menu.setText(buttonText);

        menu.setPadding(16, 44, 16, 44);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 14, 0, 14);
        menu.setLayoutParams(params);

        TextView descriptionView = new TextView(this);
        descriptionView.setText((String)descriptionMap.get(buttonText));
        descriptionView.setVisibility(View.GONE);
        descriptionView.setPadding(32, 20, 32, 20);
        descriptionView.setTextSize(14);
        descriptionView.setBackgroundResource(R.drawable.main_info);

        menu.setOnClickListener(v -> {
            if (descriptionView.getVisibility() == View.GONE) {
                descriptionView.setVisibility(View.VISIBLE);
            } else {
                descriptionView.setVisibility(View.GONE);
            }
        });

        LinearLayout container = (LinearLayout) findViewById(R.id.menu);
        container.addView(menu);
        container.addView(descriptionView);

        menuList.add(menu);
        return menu;
    }

}