package com.example.gildonaitemp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gildonaitemp.adapter.NotificationAdapter;
import com.example.gildonaitemp.adapter.NotificationItem;
import com.example.gildonaitemp.R;
import com.example.gildonaitemp.adapter.VerticalSpaceItemDecoration;
import com.example.gildonaitemp.alert.NotificationCallback;
import com.example.gildonaitemp.alert.SSEClient;
import com.example.gildonaitemp.api.ApiClient;
import com.example.gildonaitemp.api.ApiService;
import com.example.gildonaitemp.application.initApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNew, recyclerViewOld;
    private NotificationAdapter newNotificationAdapter, oldNotificationAdapter;
    private List<NotificationItem> newNotifications, oldNotifications;

    private ApiService apiService;
   // private SSEClient alertSSEClient;

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationItem item = (NotificationItem) intent.getSerializableExtra("notification");
            if (item != null) {
                runOnUiThread(() -> {
                    Log.i("Alert", "NotificationCallback");
                    newNotifications.add(0, item);
                    refreshNotification();
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ((initApplication)getApplicationContext()).resetUnreadCount();

        recyclerViewNew = findViewById(R.id.recyclerViewNewNotifications);

        apiService = ApiClient.getClient().create(ApiService.class);

        // get List<NotificationItem> 로 기존 알림 가져옴
        // 기존 알림 (샘플 데이터)
        newNotifications = new ArrayList<>();
        newNotifications.add(0, new NotificationItem("차량 점검", "소나타 12가 1234 차량의 차량 점검일이 다가옵니다. (3/14)"));
        newNotifications.add(0, new NotificationItem("안전", "오늘 졸음운전이 감지되었어요.\n"+"요즘 피곤하신가요?"));
        newNotifications.add(0, new NotificationItem("안전", "최근 운전 점수가 낮게 나왔어요.\n"+"부드러운 주행으로 점수를 올려볼까요?"));
        newNotifications.add(0, new NotificationItem("차량 소모품", "교체 완료! 소나타 12가 3456 차량 (04/04)"));

        //알림 페이지에 있을 때 알림 오면 중복 발생하는지 확인
        setNotification();

        /*alertSSEClient = new SSEClient();
        alertSSEClient.setCallback(new NotificationCallback() {
            @Override
            public void onNewNotification(NotificationItem item) {
                runOnUiThread(() -> {
                    Log.i("Alert", "NotificationCallback");
                    newNotifications.add(0, item);
                    refreshNotification();
                });
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();

/*
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        if (userId != null) {
            //alertSSEClient.startSSE(userId);

            //test
            Call<ResponseBody> call = apiService.sendTestAlert(userId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String rawJson = response.body().string();
                            Log.i("TestAlert", "원본 응답: " + rawJson);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TestAlert", "응답 실패: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestAlert", "실패: " + t.getMessage());
                }
            });
        } else {
            Log.e("NotificationActivity", "userId가 없습니다.");
        }*/
    }


    private void setNotification() {
        refreshNotification();

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
/*
    @Override
    protected void onStop() {
        super.onStop();
        alertSSEClient.stopSSE();
    }*/

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