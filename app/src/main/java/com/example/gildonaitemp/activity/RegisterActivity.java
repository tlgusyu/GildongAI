package com.example.gildonaitemp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gildonaitemp.R;
import com.example.gildonaitemp.dto.ServerUserRequest;
import com.example.gildonaitemp.dto.ServerUserResponse;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
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

        Button RegisterBtn = (Button) findViewById(R.id.RegisterBtn);
        RegisterBtn.setOnClickListener(v-> {
            String userName = etUserName.getText().toString();
            String loginId = etUserID.getText().toString();
            String password = etUserPW.getText().toString();
            if (userName.isEmpty() || loginId.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "이름, 아이디, 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(userName, loginId, password);

            }
        });
    }

    public void registerUser(String userName, String loginId, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ServerUserRequest userRequest = new ServerUserRequest(userName, loginId, password);

        Call<ServerUserResponse> call = apiService.createUser(userRequest);
        call.enqueue(new ResponseCallback<ServerUserResponse>() {
            @Override
            public void onSuccess(ServerUserResponse response) {
                Log.d("registerUser", "User created: " + response.getId());
                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Response<ServerUserResponse> response) {
                super.onError(response);
                if (response.code() == 400) {
                    Toast.makeText(RegisterActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}