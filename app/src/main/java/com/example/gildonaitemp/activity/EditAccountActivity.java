package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.dto.UserResponse;
import com.example.gildonaitemp.dto.ServerUserUpdateRequest;
import com.example.gildonaitemp.api.ResponseCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    private TextView userIdTextView;
    private EditText userNameTextView;
    private EditText userPasswordTextView;
    private EditText userPasswordCheckTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        userIdTextView = (TextView) findViewById(R.id.userId);
        userNameTextView = (EditText) findViewById(R.id.userName);
        userPasswordTextView = (EditText) findViewById(R.id.userPassword);
        userPasswordCheckTextView = (EditText) findViewById(R.id.userPasswordCheck);

        getUserInfo();
        updateUserInfo();
    }

    private void updateUserInfo() {
        Button editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(v -> {
            String newPassword = userPasswordTextView.getText().toString();
            String newPasswordCheck = userPasswordCheckTextView.getText().toString();
            if (!newPassword.equals(newPasswordCheck)) {
                Toast.makeText(EditAccountActivity.this, "비밀번호가 서로 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userId = prefs.getString("userId", null);

            if (newPassword.isEmpty()) {
                Toast.makeText(EditAccountActivity.this, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            ServerUserUpdateRequest updateRequest = new ServerUserUpdateRequest(userNameTextView.getText().toString(), newPassword);

            ApiCaller.updateUser(userId, updateRequest, new ResponseCallback<UserResponse>() {
                @Override
                public void onSuccess(UserResponse user) {
                    Toast.makeText(EditAccountActivity.this, "사용자 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditAccountActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Response<UserResponse> response) {
                    if (response.code() == 400) {
                        Toast.makeText(EditAccountActivity.this, "잘못된 요청입니다.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(EditAccountActivity.this, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditAccountActivity.this, "사용자 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(EditAccountActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    private void getUserInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        ApiCaller.getUserById(userId, new ResponseCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse user) {
                userIdTextView.setText(user.getLoginId());
                userNameTextView.setText(user.getUserName());

                if (user.getProvider().equals("KAKAO")) {
                    ImageView SNS = findViewById(R.id.isSNS);
                    SNS.setImageResource(R.drawable.button_kakao);
                    disableChange();
                }
                getCarInfo(userId);
            }

            @Override
            public void onError(Response<UserResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(EditAccountActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAccountActivity.this, "사용자 정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditAccountActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCarInfo(String userId) {
        ApiCaller.getConsumablesByUser(userId, new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    TextView carNumberTextView = findViewById(R.id.carNumber);
                    carNumberTextView.setText(consumables.get(0).getCarNumber());
                    TextView carModelTextView = findViewById(R.id.carModelName);
                    carModelTextView.setText(consumables.get(0).getCarModel());
                } else {
                    Toast.makeText(EditAccountActivity.this, "차량 정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                if (response.code() == 404) {
                    Toast.makeText(EditAccountActivity.this, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAccountActivity.this, "차량 정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                Toast.makeText(EditAccountActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        TableLayout updateUserCar = (TableLayout) findViewById(R.id.userCar);
        updateUserCar.setOnClickListener(v -> {
            Intent intent = new Intent(EditAccountActivity.this, UpdateCarActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void disableChange() {
        userPasswordTextView.setBackgroundColor(Color.TRANSPARENT);
        userPasswordCheckTextView.setBackgroundColor(Color.TRANSPARENT);
        userPasswordTextView.setHint("");
        userPasswordCheckTextView.setHint("");
        userPasswordTextView.setEnabled(false);
        userPasswordCheckTextView.setEnabled(false);

        userNameTextView.setBackgroundColor(Color.TRANSPARENT);
        userNameTextView.setFocusable(false);
    }
}