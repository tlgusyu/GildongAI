package com.example.gildonaitemp.activity;

import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.dto.CarModelResponse;
import com.example.gildonaitemp.dto.ConsumableOverviewResponse;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ResponseCallback;

import androidx.annotation.NonNull;
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
    }

    private void setNextDue() {

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getConsumableOverview(userId, new ResponseCallback<ConsumableOverviewResponse>() {
            @Override
            public void onSuccess(ConsumableOverviewResponse consumableOverview) {
                TextView preSchedule = (TextView) findViewById(R.id.preSchedule);
                TextView schedule = (TextView) findViewById(R.id.schedule);
                TextView postSchedule = (TextView) findViewById(R.id.postSchedule);

                String nextDueDate = consumableOverview.getNextDueDate();
                if (nextDueDate != null) {
                    preSchedule.setText("다음 정비 예정일은 ");
                    String nextDueDateFormat = nextDueDate.substring(0, 4) + "-" +
                            nextDueDate.substring(4, 6) + "-" +
                            nextDueDate.substring(6, 8);
                    schedule.setText(nextDueDateFormat);
                    postSchedule.setText("입니다.");
                } else {
                    preSchedule.setText("다음 정비 예정일이 없습니다");
                    schedule.setText("");
                    postSchedule.setText("");
                }

                if (consumableOverview.getAll() != null && !consumableOverview.getAll().isEmpty()) {
                    setConsumable(consumableOverview.getAll().get(0));
                } else {
                    Toast.makeText(ManageCarActivity.this, "소모품 내역을 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<ConsumableOverviewResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(ManageCarActivity.this, "사용자를 찾을 수 없습니다" + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageCarActivity.this, "차량 관리 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ConsumableOverviewResponse> call, Throwable t) {
                Toast.makeText(ManageCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void setConsumable(@NonNull ConsumableResponse data) {
        String carModel = data.getCarModel();

        TextView carNumberTextView = (TextView) findViewById(R.id.carNumber);
        TextView carModelTextView = (TextView) findViewById(R.id.carModel);
        carNumberTextView.setText(data.getCarNumber());
        carModelTextView.setText(carModel);

        SetManualBtn(carModel);

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

        ApiCaller.getCarModelsByModelName(carModel, new ResponseCallback<List<CarModelResponse>>() {
            @Override
            public void onSuccess(List<CarModelResponse> carModels) {
                if (carModels == null || carModels.isEmpty()) {
                    Toast.makeText(ManageCarActivity.this, "차량 모델 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CarModelResponse car = carModels.get(0);
                    carManual.setOnClickListener(v -> {
                        String url = car.getManualUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    });
            }

            @Override
            public void onError(Response<List<CarModelResponse>> response) {
                Toast.makeText(ManageCarActivity.this, "차량 모델 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<CarModelResponse>> call, Throwable t) {
                Toast.makeText(ManageCarActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    String safeString(String s) {
        return s == null ? "" : s;
    }

    private void addMenuButton(String buttonText, Map descriptionMap) {

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
    }

}