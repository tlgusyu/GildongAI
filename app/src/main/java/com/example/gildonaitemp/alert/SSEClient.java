package com.example.gildonaitemp.alert;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.gildonaitemp.adapter.NotificationItem;
import com.example.gildonaitemp.application.initApplication;
import com.example.gildonaitemp.dto.NotificationResponse;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.util.concurrent.TimeUnit;

public class SSEClient {

    private static SSEClient instance;
    private EventSource eventSource;
    private NotificationCallback callback;

    public void setCallback(NotificationCallback callback) {
        this.callback = callback;
    }

    private Context context;
    private boolean connected = false;
    private boolean manuallyStopped = false;

    private SSEClient(Context context) {
        this.context = context.getApplicationContext();
    }

    // 싱글톤
    public static synchronized SSEClient getInstance(Context context) {
        if (instance == null) {
            instance = new SSEClient(context);
        }
        return instance;
    }

    public void startSSE(String userId) {
        manuallyStopped = false;
        if (connected) {
            Log.i("Alert", "SSE 중복 연결 방지");
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("http://13.125.234.150:8080/alerts/subscribe/" + userId)
                .build();

        eventSource = EventSources.createFactory(client)
                .newEventSource(request, new EventSourceListener() {
                    @Override
                    public void onOpen(EventSource eventSource, Response response) {
                        Log.i("Alert", "SSE connection established for userId=" + userId);
                        connected = true;
                    }

                    @Override
                    public void onEvent(EventSource eventSource, String id, String eventType, String data) {
                        Log.i("Alert", "Received event: id=" + id + "event=" + eventType + ", data=" + data);
                        if (eventType.equals("ALERT") | eventType.equals("TEST_ALERT")) {
                            NotificationItem item = parseData(data);
                            if (callback != null && item != null) {
                                callback.onNewNotification(item);
                            }
                        }
                    }

                    @Override
                    public void onFailure(EventSource eventSource, Throwable t, Response response) {
                        Log.e("Alert", "SSE onFailure: " + t.getMessage());
                        connected = false;

                        if (manuallyStopped) {
                            Log.d("SSE", manuallyStopped + "재연결 안 함");
                            return;
                        }

                        // 1초 후에 재연결 시도
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Log.e("Alert", "SSE onFailure: try reconnect");
                            ((initApplication) context).initializeSSEConnection(userId);
                        }, 1000);
                    }

                    @Override
                    public void onClosed(EventSource eventSource) {
                        Log.i("Alert", "SSE connection closed");
                        connected = false;
                    }
                });
    }

    private NotificationItem parseData(String data) {
        Gson gson = new Gson();
        NotificationResponse parsed = gson.fromJson(data, NotificationResponse.class);
        return new NotificationItem(parsed.getTitle(), parsed.getMessage());
    }

    public void stopSSE() {
        manuallyStopped = true;
        if (eventSource != null) {
            eventSource.cancel();
            eventSource = null;
            connected = false;
            Log.i("Alert", "SSE connection manually closed");
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
