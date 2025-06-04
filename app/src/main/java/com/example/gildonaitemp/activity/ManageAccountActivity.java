package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.dto.UserResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ManageAccountActivity extends AppCompatActivity {

    ArrayList<Button> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        setUserInfo();
        setMenuBtns();
    }

    private void setUserInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getUserById(userId, new ResponseCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse user) {
                TextView usernameTextView = findViewById(R.id.username);
                usernameTextView.setText(user.getUserName());
            }

            @Override
            public void onError(Response<UserResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(ManageAccountActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageAccountActivity.this, "사용자 정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ManageAccountActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });


        ApiCaller.getConsumablesByUser(userId, new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    TextView carNumberTextView = findViewById(R.id.carNumber);
                    carNumberTextView.setText(consumables.get(0).getCarNumber());
                } else {
                    Toast.makeText(ManageAccountActivity.this, "차량 번호 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                if (response.code() == 404) {
                    Toast.makeText(ManageAccountActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ManageAccountActivity.this, "차량 번호 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                Toast.makeText(ManageAccountActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMenuBtns() {
        LinearLayout linearMenu = (LinearLayout) findViewById(R.id.menu);
        linearMenu.setOrientation(LinearLayout.VERTICAL);

        String[] buttonTexts = {"회원 정보 수정"};
        for (String text : buttonTexts) {
            linearMenu.addView(addMenuButton(text));
        }

        Button editAccount = (Button) linearMenu.getChildAt(0);
        editAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
        });
    }

    private Button addMenuButton(String s) {

        Button menu = new Button(this);
        menu.setBackgroundResource(R.drawable.menu_button);
        menu.setText(s);

        menu.setPadding(16, 44, 16, 44);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 14, 0, 14);
        menu.setLayoutParams(params);

        menuList.add(menu);
        return menu;
    }

}