package com.example.gildonaitemp.dto;

public class UserRegisterRequest {
    String userName;
    String loginId;
    String password;
    String provider;

    public UserRegisterRequest(String userName, String loginId, String password, String provider) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.provider = provider;
    }
}
