package com.example.gildonaitemp.dto;

public class NotificationRequest {
    private String userId;
    private String category;
    private String title;
    private String message;

    public NotificationRequest(String userId, String category, String title, String message) {
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.message = message;
    }


}
