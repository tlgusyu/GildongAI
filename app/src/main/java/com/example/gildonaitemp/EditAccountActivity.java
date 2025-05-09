package com.example.gildonaitemp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getUserInfo();
    }

    private void getUserInfo() {
        String userId = "qwer1234@gmail.com";
        String userName = "홍길동";

        TextView userIdTextView = (TextView) findViewById(R.id.userId);
        userIdTextView.setText(userId);
        TextView userNameTextView = (TextView) findViewById(R.id.userName);
        userNameTextView.setText(userName);



        //SNS 로그인이면 비밀번호 변경 비활성화
        TableLayout userTable = (TableLayout) findViewById(R.id.userTable);
        TableRow password = (TableRow) userTable.getChildAt(1);
        password.setBackgroundColor(Color.parseColor("#4C000000"));
        TableRow passwordCheck = (TableRow) userTable.getChildAt(2);
        passwordCheck.setBackgroundColor(Color.parseColor("#4C000000"));
        findViewById(R.id.userTablePW).setEnabled(false);
        findViewById(R.id.userPassword).setEnabled(false);
        findViewById(R.id.userTablePWCheck).setEnabled(false);
        findViewById(R.id.userPasswordCheck).setEnabled(false);




    }
}