package com.example.gildonaitemp.dto;

public class ConsumableCarUpdateRequest {
    private String userId;
    private String carModel;
    private String carNumber;

    public ConsumableCarUpdateRequest(String userId, String carModel, String carNumber) {
        this.userId = userId;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }
}
