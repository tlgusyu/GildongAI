package com.example.gildonaitemp.dto;

public class ServerUserResponse {
    private String id;
    private String loginId;
    private String userName;
    private int avgDrivingScore;
    private String createdAt;

    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getUserName() {
        return userName;
    }

    public int getAvgDrivingScore() {
        return avgDrivingScore;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
