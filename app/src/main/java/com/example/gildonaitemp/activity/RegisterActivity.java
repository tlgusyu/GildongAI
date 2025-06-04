package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.dto.UserRegisterRequest;
import com.example.gildonaitemp.dto.UserResponse;
import com.example.gildonaitemp.api.ResponseCallback;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        enterInfo();
    }

    private void enterInfo() {
        EditText etUserName = (EditText) findViewById(R.id.userName);
        EditText etUserID = (EditText) findViewById(R.id.userID);
        EditText etUserPW = (EditText) findViewById(R.id.userPW);
        EditText etUserPWCheck = (EditText) findViewById(R.id.userPWcheck);

        Button RegisterBtn = (Button) findViewById(R.id.RegisterBtn);
        RegisterBtn.setOnClickListener(v-> {
            String userName = etUserName.getText().toString();
            String loginId = etUserID.getText().toString();
            String password = etUserPW.getText().toString();
            String passwordCheck = etUserPWCheck.getText().toString();
            if (userName.isEmpty() || loginId.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "이름, 아이디, 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passwordCheck)){
                Toast.makeText(RegisterActivity.this, "비밀번호가 서로 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(userName, loginId, password);
            }
        });
    }

    public void registerUser(String userName, String loginId, String password) {
        UserRegisterRequest userRequest = new UserRegisterRequest(userName, loginId, password, "LOCAL");

        ApiCaller.registerUser(userRequest, new ResponseCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse response) {
                // 일반 회원 등록
                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<UserResponse> response) {
                if (response.code() == 400) {
                    Toast.makeText(RegisterActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 409) {
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}