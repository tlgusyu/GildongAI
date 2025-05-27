package com.example.gildonaitemp.dto;

public class ConsumableRequest {
    private String userId;
    private String carModel;
    private String carNumber;

    public ConsumableRequest(String userId, String carModel, String carNumber) {
        this.userId = userId;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }
}
