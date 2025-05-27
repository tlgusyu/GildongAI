package com.example.gildonaitemp.dto;

public class ServerUserLoginRequest {
    String loginId;
    String password;

    public ServerUserLoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}