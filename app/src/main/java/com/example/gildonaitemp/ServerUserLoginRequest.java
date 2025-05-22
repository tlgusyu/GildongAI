package com.example.gildonaitemp;

public class ServerUserLoginRequest {
    String loginId;
    String password;

    public ServerUserLoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}