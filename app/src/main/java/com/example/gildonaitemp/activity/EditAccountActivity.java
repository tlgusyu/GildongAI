package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.dto.ServerUserResponse;
import com.example.gildonaitemp.dto.ServerUserUpdateRequest;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.api.ResponseCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

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

    private TextView userIdTextView;
    private EditText userNameTextView;
    private EditText userPasswordTextView;
    private EditText userPasswordCheckTextView;

    private void updateUserInfo() {
        Button editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(v -> {
            String newPassword = userPasswordTextView.getText().toString();
            String newPasswordCheck = userPasswordCheckTextView.getText().toString();
            if (!newPassword.equals(newPasswordCheck)) {
                Toast.makeText(EditAccountActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userId = prefs.getString("userId", null);

            if (newPassword.isEmpty()) {
                Toast.makeText(EditAccountActivity.this, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            ServerUserUpdateRequest updateRequest = new ServerUserUpdateRequest(userNameTextView.getText().toString(), newPassword);
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            Call<ServerUserResponse> call = apiService.updateUser(userId, updateRequest);
            call.enqueue(new ResponseCallback<ServerUserResponse>() {
                @Override
                public void onSuccess(ServerUserResponse user) {
                    Log.d("updateUser", "수정 성공: " + user.getUserName());
                    Toast.makeText(EditAccountActivity.this, "사용자 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Response<ServerUserResponse> response) {
                    super.onError(response);  // 에러 로그 및 파싱

                    if (response.code() == 400) {
                        Toast.makeText(EditAccountActivity.this, "잘못된 요청입니다.", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(EditAccountActivity.this, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditAccountActivity.this, "사용자 정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerUserResponse> call, Throwable t) {
                    super.onFailure(call, t);
                    Toast.makeText(EditAccountActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void getUserInfo() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        Log.d("temp", userId);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ServerUserResponse> callUser = apiService.getUserById(userId);

        callUser.enqueue(new ResponseCallback<ServerUserResponse>() {
            @Override
            public void onSuccess(ServerUserResponse user) {
                userIdTextView.setText(user.getLoginId());
                userNameTextView.setText(user.getUserName());

                //임시
                String provider = "LOCAL";
                if(provider.equals("KAKAO")) { //SNS 로그인이면 비밀번호 변경 비활성화
                    ImageView SNS = (ImageView) findViewById(R.id.isSNS);
                    SNS.setImageResource(R.drawable.button_kakao);
                    disableChangePW();
                }
            }

            @Override
            public void onError(Response<ServerUserResponse> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Log.e("fetchUserById", "사용자 없음(에러 코드 : 401)");
                } else {
                    Log.e("fetchUserById", "사용자 정보 조회 실패");
                }
            }

            @Override
            public void onFailure(Call<ServerUserResponse> call, Throwable t) {
                super.onFailure(call, t);
                Log.e("fetchUserById", "네트워크 오류", t);
            }
        });

        Call<List<ConsumableResponse>> callCar = apiService.getConsumablesByUser(userId);
        callCar.enqueue(new ResponseCallback<List<ConsumableResponse>>() {
            @Override
            public void onSuccess(List<ConsumableResponse> consumables) {
                if (consumables != null && !consumables.isEmpty()) {
                    TextView carNumberTextView = (TextView) findViewById(R.id.carNumber);
                    carNumberTextView.setText(consumables.get(3).getCarNumber()); //최신으로
                    TextView carModelTextView = (TextView) findViewById(R.id.carModelName);
                    carModelTextView.setText(consumables.get(3).getCarModel()); //최신으로
                } else {
                    Log.w("fetchConsumables", "소모품 내역 조회 실패");
                    Toast.makeText(EditAccountActivity.this, "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<List<ConsumableResponse>> response) {
                super.onError(response);
                if (response.code() == 404) {
                    Toast.makeText(EditAccountActivity.this, "사용자 없음(에러 코드 : " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditAccountActivity.this, "소모품 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(EditAccountActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });

        TableLayout updateUserCar = (TableLayout) findViewById(R.id.userCar);
        updateUserCar.setOnClickListener(v -> {
            Intent intent = new Intent(EditAccountActivity.this, UpdateCarActivity.class);
            startActivity(intent);
            //finish();
        });

    }

    private void disableChangePW() {
        userPasswordTextView.setBackgroundColor(Color.TRANSPARENT);
        userPasswordCheckTextView.setBackgroundColor(Color.TRANSPARENT);
        userPasswordTextView.setHint("");
        userPasswordCheckTextView.setHint("");
        userPasswordTextView.setEnabled(false);
        userPasswordCheckTextView.setEnabled(false);
    }
}