package com.example.gildonaitemp.application;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gildonaitemp.adapter.NotificationItem;
import com.example.gildonaitemp.alert.NotificationCallback;
import com.example.gildonaitemp.alert.SSEClient;
import com.kakao.sdk.common.KakaoSdk;

public class initApplication extends Application implements DefaultLifecycleObserver {

    private SSEClient sseClient;
    private String userId;
    private int unreadNotificationCount = 0;

    public synchronized int getUnreadNotificationCount() {
        return unreadNotificationCount;
    }

    public synchronized void incrementUnreadCount() {
        unreadNotificationCount++;
    }

    public synchronized void resetUnreadCount() {
        unreadNotificationCount = 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "092e3b2df3d1065f7f68be2254ba62f9");

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", null);

        if (userId != null) {
            initializeSSEConnection(userId);
        }

    }

    public void initializeSSEConnection(String userId) {
        if (sseClient == null) {
            sseClient = new SSEClient();
            sseClient.setCallback(new NotificationCallback() {
                @Override
                public void onNewNotification(NotificationItem item) {
                    // POST (new NotificationItem)

                    ((initApplication)getApplicationContext()).incrementUnreadCount();

                    // Broadcast 전송
                    Intent intent = new Intent("NEW_NOTIFICATION");
                    intent.putExtra("notification", item);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    Intent countIntent = new Intent("UNREAD_COUNT_UPDATED");
                    countIntent.putExtra("count", ((initApplication)getApplicationContext()).getUnreadNotificationCount());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(countIntent);
                }
            });
        }
        sseClient.startSSE(userId);

/*
        // 로그인 직후 아래 코드 추가
        sseClient = new SSEClient();
        sseClient.setCallback(new NotificationCallback() {
            @Override
            public void onNewNotification(NotificationItem item) {
                // POST (new NotificationItem) 으로 변경
                ((initApplication)getApplicationContext()).incrementUnreadCount();

                // Broadcast 전송
                Intent intent = new Intent("NEW_NOTIFICATION");
                intent.putExtra("notification", item);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                Intent countIntent = new Intent("UNREAD_COUNT_UPDATED");
                countIntent.putExtra("count", ((initApplication)getApplicationContext()).getUnreadNotificationCount());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(countIntent);
            }
        });*/
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (userId != null) {
            sseClient.startSSE(userId);
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        sseClient.stopSSE();
    }


}
