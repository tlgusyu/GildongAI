package com.example.gildonaitemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {

    ArrayList<Button> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        setUserInfo();
        setMenuBtns();
    }

    private void setMenuBtns() {
        LinearLayout linearMenu = (LinearLayout) findViewById(R.id.menu);
        linearMenu.setOrientation(LinearLayout.VERTICAL);

        String[] buttonTexts = {"나의 운전 습관", "회원 정보 수정"};
        for (String text : buttonTexts) {
            linearMenu.addView(addMenuButton(text));
        }

        Button myDriving = (Button) linearMenu.getChildAt(0);


        Button editAccount = (Button) linearMenu.getChildAt(1);
        editAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
        });
    }

    private void setUserInfo() {
        //임시
        String username = "홍길동";
        String carNumber = "12가 3456";

        TextView usernameTextView = (TextView) findViewById(R.id.username);
        TextView carNumberTextView = (TextView) findViewById(R.id.carNumber);
        usernameTextView.setText(username);
        carNumberTextView.setText(carNumber);
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