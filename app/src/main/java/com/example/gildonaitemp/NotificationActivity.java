package com.example.gildonaitemp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNew, recyclerViewOld;
    private NotificationAdapter newNotificationAdapter, oldNotificationAdapter;
    private List<NotificationItem> newNotifications, oldNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerViewNew = findViewById(R.id.recyclerViewNewNotifications);
        recyclerViewOld = findViewById(R.id.recyclerViewOldNotifications);

        // 새로운 알림 샘플 데이터
        newNotifications = new ArrayList<>();
        newNotifications.add(new NotificationItem("차량 점검", "차량 점검", "소나타 12가 1234 차량의 차량 점검일이 다가옵니다. (3/14)"));
        newNotifications.add(new NotificationItem("안전", "안전", "오늘 졸음운전이 감지되었어요.\n"+"요즘 피곤하신가요?"));
        newNotifications.add(new NotificationItem("안전", "안전", "최근 운전 점수가 낮게 나왔어요.\n"+"부드러운 주행으로 점수를 올려볼까요?"));
        newNotifications.add(new NotificationItem("차량 소모품", "차량 소모품", "교체 완료! 소나타 12가 3456 차량 (04/04)"));

        // 지난 알림 샘플 데이터
        oldNotifications = new ArrayList<>();
        oldNotifications.add(new NotificationItem("차량 소모품", "차량 소모품", "교체 완료! 소나타 12가 1234 차량 (3/10)"));
        oldNotifications.add(new NotificationItem("차량 점검", "차량 점검", "소나타 12가 3456 의 차량 점검일이 다가옵니다. (3/30)"));
        oldNotifications.add(new NotificationItem("차량 점검", "차량 점검", "점검 완료! 소나타 12가 3456 차량 (3/30)"));

        // 어댑터 연결
        newNotificationAdapter = new NotificationAdapter(newNotifications);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNew.setAdapter(newNotificationAdapter);

        oldNotificationAdapter = new NotificationAdapter(oldNotifications);
        recyclerViewOld.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOld.setAdapter(oldNotificationAdapter);
    }
}