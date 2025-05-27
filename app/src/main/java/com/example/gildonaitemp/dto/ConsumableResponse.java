package com.example.gildonaitemp.dto;

import java.io.Serializable;

public class ConsumableResponse implements Serializable {
    private String id;
    private String userId;
    private String carModel;
    private String carNumber;
    private String engineOilDate;
    private String batteryDate;
    private String coolantDate;
    private String transmissionOilDate;
    private String brakeOilDate;
    private String washerFluidDate;
    private String createdAt;
    private String updatedAt;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getCarModel() { return carModel; }
    public String getCarNumber() { return carNumber; }
    public String getEngineOilDate() { return engineOilDate; }
    public String getBatteryDate() { return batteryDate; }
    public String getCoolantDate() { return coolantDate; }
    public String getTransmissionOilDate() { return transmissionOilDate; }
    public String getBrakeOilDate() { return brakeOilDate; }
    public String getWasherFluidDate() { return washerFluidDate; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}
