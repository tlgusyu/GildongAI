package com.example.gildonaitemp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.dto.ConsumableResponse;
import com.example.gildonaitemp.dto.KakaoUserRequest;
import com.example.gildonaitemp.dto.ServerUserLoginRequest;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.dto.ServerUserRequest;
import com.example.gildonaitemp.dto.ServerUserResponse;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;

import com.example.gildonaitemp.api.ResponseCallback;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;

import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.common.util.Utility;

import java.security.MessageDigest;
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

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
            for (Signature signature : info.signingInfo.getApkContentsSigners()) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                Log.d("KeyHash", "KeyHash: " + keyHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // 공통 콜백: 카카오 로그인 결과 처리
        Function2<OAuthToken, Throwable, Unit> callback = (token, error) -> {
            Log.d("KakaoLogin", "callback 진입"); //임시
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
                Toast.makeText(LoginActivity.this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show();
            } else if (token != null) {
                Log.i(TAG, "로그인 성공: " + token.getAccessToken());
                Toast.makeText(LoginActivity.this, "카카오 로그인 성공", Toast.LENGTH_SHORT).show(); //임시
                UserApiClient.getInstance().me((user, meError) -> {
                    Toast.makeText(getApplicationContext(), "callback 진입", Toast.LENGTH_SHORT).show(); //임시
                    if (meError != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", meError);
                    } else if (user != null) {
                        String userId = user.getId().toString();
                        String loginId = user.getKakaoAccount().getEmail();
                        String userName = user.getKakaoAccount().getProfile().getNickname();

                        Log.i(TAG, "사용자 정보 요청 성공:\n" +
                                "ID: " + userId + "\n" +
                                "Email: " + loginId + "\n" +
                                "Nickname: " + userName);

                        // TODO: 사용자 정보로 로그인하거나, 회원가입 후 로그인
                        // get user by userid
                        // if user is null
                        //     register
                        // login

                        ApiService apiService = ApiClient.getClient().create(ApiService.class);
                        Call<ServerUserResponse> call = apiService.getUserById(userId);

                        call.enqueue(new ResponseCallback<ServerUserResponse>() {
                            @Override
                            public void onSuccess(ServerUserResponse user) {
                                Log.i(TAG, "기존 사용자 로그인 성공");
                                Toast.makeText(LoginActivity.this, "카카오 로그인 성공(기존 사용자)", Toast.LENGTH_SHORT).show(); //임시
                                loginSuccess(user);
                            }
                            @Override
                            public void onError(Response<ServerUserResponse> response) {
                                super.onError(response);
                                if (response.code() == 404) {
                                    ServerUserRequest newUser = new ServerUserRequest(userName, loginId, "kakao_temp_pw");
                                    apiService.createUser(newUser).enqueue(new ResponseCallback<ServerUserResponse>() {
                                        @Override
                                        public void onSuccess(ServerUserResponse newUserResponse) {
                                            Log.i(TAG, "신규 사용자 회원가입 및 로그인 성공");
                                            Toast.makeText(LoginActivity.this, "카카오 회원가입 성공", Toast.LENGTH_SHORT).show(); //임시
                                            loginSuccess(newUserResponse);
                                        }

                                        @Override
                                        public void onError(Response<ServerUserResponse> response) {
                                            Log.e(TAG, "회원가입 실패: " + response.code());
                                        }
                                    });
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
                    Log.e(TAG, "카카오톡으로 로그인 실패", error);
                    Toast.makeText(LoginActivity.this, "카카오톡 로그인 실패", Toast.LENGTH_SHORT).show(); //임시

                    // 사용자가 로그인 취소한 경우
                    if (error instanceof ClientError &&
                            ((ClientError) error).getReason() == ClientErrorCause.Cancelled) {
                        return Unit.INSTANCE;
                    }

                    // 실패 시 카카오계정 로그인으로 대체
                    Toast.makeText(LoginActivity.this, "카카오 계정 로그인 시도", Toast.LENGTH_SHORT).show(); //임시
                    UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                } else {
                    // 성공 시 공통 콜백 사용
                    callback.invoke(token, null);
                }
                return Unit.INSTANCE;
            });
        } else {
            // 카카오톡 미설치: 바로 카카오계정 로그인
            Toast.makeText(LoginActivity.this, "카카오 계정 로그인 시도", Toast.LENGTH_SHORT).show(); //임시
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
        }
    }



/*
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
        }*/


}