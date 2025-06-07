package com.example.gildonaitemp.alert;

import android.util.Log;

import com.example.gildonaitemp.adapter.NotificationItem;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

import java.util.concurrent.TimeUnit;

public class SSEClient {

    private EventSource eventSource;
    private NotificationCallback callback;

    public void setCallback(NotificationCallback callback) {
        this.callback = callback;
    }

    public void startSSE(String userId) {
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
                    }

                    @Override
                    public void onEvent(EventSource eventSource, String id, String eventType, String data) {
                        Log.i("Alert", "Received event: id=" + id + "event=" + eventType + ", data=" + data);
                        if (eventType.equals("ALERT")) {
                            NotificationItem item = parseData(data);
                            if (callback != null && item != null) {
                                callback.onNewNotification(item);
                            }
                        }
                    }

                    @Override
                    public void onFailure(EventSource eventSource, Throwable t, Response response) {
                        Log.e("Alert", "SSE onFailure: " + t.getMessage());
                    }

                    @Override
                    public void onClosed(EventSource eventSource) {
                        Log.i("Alert", "SSE connection closed");
                    }
                });
    }

    private NotificationItem parseData(String data) {
        Gson gson = new Gson();
        AlertData parsed = gson.fromJson(data, AlertData.class);
        return new NotificationItem(parsed.title, parsed.message);
    }

    public void stopSSE() {
        if (eventSource != null) {
            eventSource.cancel();
            eventSource = null;
            Log.i("Alert", "SSE connection manually closed");
        }
    }
}
