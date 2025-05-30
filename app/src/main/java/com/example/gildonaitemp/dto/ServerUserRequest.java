package com.example.gildonaitemp.dto;

public class ServerUserRequest {
    String userName;
    String loginId;
    String password;
    String provider;

    public ServerUserRequest(String userName, String loginId, String password, String provider) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.provider = provider;
    }
}
