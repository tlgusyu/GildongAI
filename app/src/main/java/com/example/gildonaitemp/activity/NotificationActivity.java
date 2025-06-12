package com.example.gildonaitemp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.adapter.NotificationAdapter;
import com.example.gildonaitemp.adapter.NotificationItem;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.adapter.VerticalSpaceItemDecoration;
import com.example.gildonaitemp.api.ApiCaller;
import com.example.gildonaitemp.api.ResponseCallback;
import com.example.gildonaitemp.application.initApplication;
import com.example.gildonaitemp.dto.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNew;
    private NotificationAdapter newNotificationAdapter;
    private List<NotificationItem> newNotifications = new ArrayList<>();;

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationItem item = (NotificationItem) intent.getSerializableExtra("notification");
            if (item != null) {
                runOnUiThread(() -> {
                    Log.i("Alert", "NotificationCallback on Broadcast");
                    newNotifications.clear();
                    getAlertHistory();
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ((initApplication)getApplicationContext()).resetUnreadCount();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((initApplication)getApplicationContext()).resetUnreadCount();
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        recyclerViewNew = findViewById(R.id.recyclerViewNewNotifications);

        getAlertHistory();
        setNotification();
    }

    private void getAlertHistory() {

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        // 기존 알림 가져온 후 화면에 적용
        ApiCaller.getNotificationsByUserId(userId, new ResponseCallback<List<NotificationResponse>>() {
            @Override
            public void onSuccess(List<NotificationResponse> notifications) {
                for (NotificationResponse notification : notifications) {
                    newNotifications.add(new NotificationItem(notification.getTitle(), notification.getMessage()));
                }
                refreshNotification();
            }

            @Override
            public void onError(Response<List<NotificationResponse>> response) {
                if (response.code() == 404) {
                    Toast.makeText(NotificationActivity.this, "사용자 또는 알림을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificationActivity.this, "알림 내역 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "서버 연결에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setNotification() {
        // 16dp 간격 적용 (ItemDecoration)
        int spacingInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        recyclerViewNew.addItemDecoration(new VerticalSpaceItemDecoration(spacingInPixels));
    }

    private void refreshNotification() {
        // 어댑터 연결
        newNotificationAdapter = new NotificationAdapter(newNotifications);
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNew.setAdapter(newNotificationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(notificationReceiver, new IntentFilter("NEW_NOTIFICATION"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(notificationReceiver);
    }
}