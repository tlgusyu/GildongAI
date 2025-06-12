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

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (sseClient != null) {
            sseClient.stopSSE();
        }
    }

}
