package com.example.gildonaitemp.dto;

public class NotificationResponse {
        String id;
        String userId;
        String category;
        String title;
        String message;
        String createdAt;

        public String getTitle() { return title; }
        public String getMessage() { return message; }
}
