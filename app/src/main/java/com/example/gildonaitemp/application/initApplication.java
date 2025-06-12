package com.example.gildonaitemp.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gildonaitemp.adapter.NotificationItem;
import com.example.gildonaitemp.alert.NotificationCallback;
import com.example.gildonaitemp.alert.SSEClient;
import com.kakao.sdk.common.KakaoSdk;

public class initApplication extends Application implements DefaultLifecycleObserver {

    private SSEClient sseClient;
    private int unreadNotificationCount = 0;
    private String userId;

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

        Log.i("Alert", "initApplication onCreate");
    }

    public void initializeSSEConnection(String userId) {
        setUserId(userId);

        Log.i("Alert", "initApplication initializeSSEConnection get userId " + userId);
        if (sseClient == null) {
            sseClient = SSEClient.getInstance(this);
            sseClient.setCallback(new NotificationCallback() {
                @Override
                public void onNewNotification(NotificationItem item) {
                    incrementUnreadCount();

                    // BroadCast 전송
                    Intent intent = new Intent("NEW_NOTIFICATION");
                    intent.putExtra("notification", item);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    Intent countIntent = new Intent("UNREAD_COUNT_UPDATED");
                    countIntent.putExtra("count", getUnreadNotificationCount());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(countIntent);
                }
            });
        }
        sseClient.startSSE(userId);

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.d("Alert", "앱이 포그라운드로 돌아옴 userId: " + userId);
        if ((sseClient != null) && (userId != null)) {
            sseClient.startSSE(userId);
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.d("Alert", "앱이 백그라운드로 전환됨");
        if (sseClient != null) {
            sseClient.stopSSE();
        }
    }

}
