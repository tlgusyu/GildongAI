package com.example.gildonaitemp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ManageCarActivity extends AppCompatActivity {

    ArrayList<Button> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);

        setCarInfo();
        setMenuBtns();
    }

    private void setMenuBtns() {
        // 임시 (서버에서 받아와야하는 정보)
        String carType = "소나타";
        String scheduled = "4/16 일요일";

        LinearLayout linearMenu = (LinearLayout) findViewById(R.id.menu);
        linearMenu.setOrientation(LinearLayout.VERTICAL);

        String[] buttonTexts = {carType + " 차량 메뉴얼 보기",
                "타이어 관리", "엔진 오일 관리", "기타 소모품 관리",
                "다음 정비 예정일은 " + scheduled + " 입니다.",
                "정비 내역 조회", "사고 기록 조회"};
        for (String text : buttonTexts) {
            linearMenu.addView(addMenuButton(text));
        }

        SpannableStringBuilder carTypeBoldText = new SpannableStringBuilder();
        SpannableString carTypeBold = new SpannableString(carType);
        carTypeBold.setSpan(new StyleSpan(Typeface.BOLD), 0, carType.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        carTypeBoldText.append(carTypeBold);
        carTypeBoldText.append(" 차량 메뉴얼 보기");
        menuList.get(0).setText(carTypeBoldText);

        SpannableStringBuilder scheduledBoldText = new SpannableStringBuilder();
        scheduledBoldText.append("다음 정비 예정일은 ");
        SpannableString scheduledBold = new SpannableString(scheduled);
        scheduledBold.setSpan(new StyleSpan(Typeface.BOLD), 0, scheduled.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        scheduledBoldText.append(scheduledBold);
        scheduledBoldText.append(" 입니다");
        menuList.get(4).setText(scheduledBoldText);
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

    private void setCarInfo() {
        //임시
        String carNumber = "12가 3456";
        String carType = "소나타";

        TextView carNumberTextView = (TextView) findViewById(R.id.carNumber);
        TextView carTypeTextView = (TextView) findViewById(R.id.carType);
        carNumberTextView.setText(carNumber);
        carTypeTextView.setText(carType);
    }

}