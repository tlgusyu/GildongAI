package com.example.gildonaitemp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.ConsumableResponse;
import com.example.gildonaitemp.ServerUserLoginRequest;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.ServerUserResponse;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;

import com.example.gildonaitemp.api.ResponseCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUserID, etPassword;
    private Button loginButton, registerButton;
    private LinearLayout kakaoLogin;
            //, naverLogin, googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setButton();
    }

    private void loginSuccess(ServerUserResponse user) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", user.getId());
        editor.apply();

        String userId = prefs.getString("userId", null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<ConsumableResponse>> call = apiService.getConsumablesByUser(userId);

        call.enqueue(new Callback<List<ConsumableResponse>>() {
            @Override
            public void onResponse(Call<List<ConsumableResponse>> call, Response<List<ConsumableResponse>> response) {
                if (response.isSuccessful()) {
                    List<ConsumableResponse> consumables = response.body();

                    if (consumables == null || consumables.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), RegisterCarActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Log.e("Consumable", "조회 실패: 코드 " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ConsumableResponse>> call, Throwable t) {
                Log.e("Consumable", "서버 통신 실패", t);
            }
        });

        finish();
    }

    private void setButton() {
        registerButton = findViewById(R.id.sign_up);
        loginButton = findViewById(R.id.login_button);
        kakaoLogin = findViewById(R.id.signup_kakao);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextInputLayout layoutID  = findViewById(R.id.LayoutID);
        TextInputLayout layoutPW  = findViewById(R.id.LayoutPW);
        etUserID = findViewById(R.id.ID);
        etPassword = findViewById(R.id.PW);

        etUserID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutID.setHint("");
                } else if (etUserID.getText().toString().isEmpty()){
                    layoutID.setHint("아이디");
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layoutPW.setHint("");
                } else if (etPassword.getText().toString().isEmpty()){
                    layoutPW.setHint("비밀번호");
                }
            }
        });

        loginButton.setOnClickListener(v -> {
            String loginId = etUserID.getText().toString();
            String password = etPassword.getText().toString();

            if (loginId.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(loginId, password);
            }
        });

        kakaoLogin.setOnClickListener(v -> kakaoAccess());
    }

    public void loginUser(String loginId, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ServerUserLoginRequest loginRequest = new ServerUserLoginRequest(loginId, password);

        Call<ServerUserResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new ResponseCallback<ServerUserResponse>() {
            @Override
            public void onSuccess(ServerUserResponse user) {
                Log.d("loginUser", "로그인 성공: " + user.getUserName());
                loginSuccess(user);
            }

            @Override
            public void onError(Response<ServerUserResponse> response) {
                super.onError(response);
                if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerUserResponse> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(LoginActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void kakaoAccess() {
        final String TAG = "KakaoLogin";
        Context context = this;

        // 공통 콜백 정의
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken token, Throwable error) {
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error);
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 " + token.getAccessToken());
                    //loginSuccess();
                }
                return Unit.INSTANCE;
            }
        };

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
            UserApiClient.getInstance().loginWithKakaoTalk(context, new Function2<OAuthToken, Throwable, Unit>() {
                @Override
                public Unit invoke(OAuthToken token, Throwable error) {
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error);

                        // 사용자가 로그인 취소한 경우
                        if (error instanceof ClientError) {
                            ClientError clientError = (ClientError) error;
                            if (clientError.getReason() == ClientErrorCause.Cancelled) {
                                return Unit.INSTANCE;  // 로그인 취소 처리
                            }
                        }

                        // 카카오계정으로 로그인 시도
                        UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 " + token.getAccessToken());
                        //loginSuccess();
                    }
                    return Unit.INSTANCE;
                }
            });
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
        }

    }

}