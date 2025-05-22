package com.example.gildonaitemp;

public class ServerUserRequest {
    String userName;
    String loginId;
    String password;

    public ServerUserRequest(String userName, String loginId, String password) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
    }
}
