package com.example.gildonaitemp.adapter;

public class NotificationItem {
    private String type;        // 알림 종류 (차량 점검, 안전, 차량 소모품, 지난 알림)
    private String category;    // 알림 카테고리 (차량 점검, 안전, 차량 소모품)
    private String message;     // 알림 텍스트 (설명)

    public NotificationItem(String type, String category, String message) {
        this.type = type;
        this.category = category;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }
}
