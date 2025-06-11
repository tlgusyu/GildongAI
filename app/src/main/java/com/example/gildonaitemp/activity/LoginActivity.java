package com.example.gildonaitemp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.application.initApplication;
import com.example.gildonaitemp.dto.UserRegisterRequest;
import com.example.gildonaitemp.dto.UserResponse;
import com.example.gildonaitemp.api.ResponseCallback;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import retrofit2.Call;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setButton();
    }

    private void loginSuccess(UserResponse user) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", user.getId());
        editor.apply();

        ((initApplication)getApplicationContext()).initializeSSEConnection(user.getId());

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setButton() {
        Button registerButton = (Button) findViewById(R.id.sign_up);
        Button loginButton = (Button) findViewById(R.id.login_button);
        LinearLayout kakaoLogin = (LinearLayout) findViewById(R.id.signup_kakao);
        LinearLayout naverLogin = (LinearLayout) findViewById(R.id.signup_naver);
        LinearLayout googleLogin = (LinearLayout) findViewById(R.id.signup_google);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextInputLayout layoutID  = findViewById(R.id.LayoutID);
        TextInputLayout layoutPW  = findViewById(R.id.LayoutPW);
        TextInputEditText etUserID, etPassword;
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

        kakaoLogin.setOnClickListener(v ->
                kakaoAccess()
        );

        naverLogin.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "서비스 개발 중입니다", Toast.LENGTH_SHORT).show();
        });

        googleLogin.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "서비스 개발 중입니다", Toast.LENGTH_SHORT).show();
        });
    }

    public void loginUser(String loginId, String password) {
        ApiCaller.loginUser(loginId, password, new ResponseCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse user) {
                // 일반 회원 로그인 성공
                loginSuccess(user);
            }

            @Override
            public void onError(Response<UserResponse> response) {
                if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void kakaoAccess() {
        findViewById(android.R.id.content).setAlpha(0f);

        final String TAG = "KakaoLogin";
        Context context = this;

        // 공통 콜백: 카카오 로그인 결과 처리
        Function2<OAuthToken, Throwable, Unit> callback = (token, error) -> {
            if (error != null) {
                Toast.makeText(LoginActivity.this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show();
            } else if (token != null) {
                UserApiClient.getInstance().me((user, meError) -> {
                    if (meError != null) {
                        Toast.makeText(LoginActivity.this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show();
                    } else if (user != null) {
                        String loginId = user.getKakaoAccount().getEmail();
                        String userName = user.getKakaoAccount().getProfile().getNickname();

                        ApiCaller.getUserByLoginId(loginId, new ResponseCallback<UserResponse>() {
                            @Override
                            public void onSuccess(UserResponse user) {
                                // 기존 카카오 회원 로그인 성공
                                Toast.makeText(LoginActivity.this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show();
                                loginSuccess(user);
                            }
                            @Override
                            public void onError(Response<UserResponse> response) {
                                if (response.code() == 404) {
                                    UserRegisterRequest kakaoRequest = new UserRegisterRequest(userName, loginId, null, "KAKAO");

                                    ApiCaller.registerUser(kakaoRequest, new ResponseCallback<UserResponse>() {
                                        @Override
                                        public void onSuccess(UserResponse newUserResponse) {
                                            // 신규 카카오 회원 등록 및 로그인 성공
                                            Toast.makeText(LoginActivity.this, "카카오 회원가입 및 로그인 성공", Toast.LENGTH_SHORT).show();
                                            loginSuccess(newUserResponse);
                                        }

                                        @Override
                                        public void onError(Response<UserResponse> response) {
                                            if (response.code() == 400) {
                                                Toast.makeText(LoginActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                                            }
                                            Toast.makeText(LoginActivity.this, "카카오 회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<UserResponse> call, Throwable t) {
                                            Toast.makeText(LoginActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(LoginActivity.this, "사용자 정보를 불러오지 못했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    return null;
                });
            }
            return Unit.INSTANCE;
        };

        // 카카오톡 로그인 가능 여부 확인
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
            UserApiClient.getInstance().loginWithKakaoTalk(context, (token, error) -> {
                if (error != null) {
                    Toast.makeText(LoginActivity.this, "카카오톡 로그인 실패", Toast.LENGTH_SHORT).show();

                    // 사용자가 로그인 취소한 경우
                    if (error instanceof ClientError &&
                            ((ClientError) error).getReason() == ClientErrorCause.Cancelled) {
                        return Unit.INSTANCE;
                    }

                    // 실패 시 카카오 계정 로그인으로 대체
                    UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                } else {
                    // 성공 시 공통 콜백 사용
                    callback.invoke(token, null);
                }
                return Unit.INSTANCE;
            });
        } else {
            // 카카오톡 미설치: 바로 카카오계정 로그인
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
        }
    }

}