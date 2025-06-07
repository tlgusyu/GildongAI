package com.example.gildonaitemp.adapter;

import java.io.Serializable;

public class NotificationItem implements Serializable {
    private String title;    // 차량 점검, 안전, 차량 소모품
    private String message;     // 알림 텍스트 (설명)

    public NotificationItem(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
