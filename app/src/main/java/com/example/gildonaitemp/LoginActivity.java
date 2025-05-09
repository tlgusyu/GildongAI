package com.example.gildonaitemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText idText;
    private TextInputEditText passwordText;
    private EditText etUsername, etPassword;
    private Button loginButton, registerButton;
    private LinearLayout kakaoLogin, naverLogin, googleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setButton();
    }

    private void setButton() {
        etUsername = findViewById(R.id.ID);
        etPassword = findViewById(R.id.PW);
        registerButton = findViewById(R.id.sign_up);
        loginButton = findViewById(R.id.login_button);
        kakaoLogin = findViewById(R.id.signup_kakao);
        naverLogin = findViewById(R.id.signup_naver);
        googleLogin = findViewById(R.id.signup_google);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);

        });

        loginButton.setOnClickListener(v -> {
//            String username = etUsername.getText().toString();
//            String password = etPassword.getText().toString();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
//            } else {
//                // 서버로 로그인 요청
//                loginUser(username, password);
//            }

            //임시
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        });






    }

}