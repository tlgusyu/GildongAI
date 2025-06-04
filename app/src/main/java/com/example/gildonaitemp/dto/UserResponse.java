package com.example.gildonaitemp.dto;

public class UserResponse {
    private String id;
    private String loginId;
    private String userName;
    private double avgDrivingScore;
    private String createdAt;
    private String provider;

    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getUserName() {
        return userName;
    }

    public double getAvgDrivingScore() {
        return avgDrivingScore;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getProvider() {
        return provider;
    }
}
