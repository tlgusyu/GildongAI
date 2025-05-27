package com.example.gildonaitemp.dto;

public class ServerUserUpdateRequest {
    private String userName;
    private String password;

    public ServerUserUpdateRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
