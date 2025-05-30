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
    private String airconFilterDate;

    private String engineOilChangedDate;
    private String batteryChangedDate;
    private String coolantChangedDate;
    private String transmissionOilChangedDate;
    private String brakeOilChangedDate;
    private String airconFilterChangedDate;

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
    public String getAirconFilterDate() { return airconFilterDate; }
    public String getEngineOilChangedDate() { return engineOilChangedDate; }
    public String getBatteryChangedDate() { return batteryChangedDate; }
    public String getCoolantChangedDate() { return coolantChangedDate; }
    public String getTransmissionOilChangedDate() { return transmissionOilChangedDate; }
    public String getBrakeOilChangedDate() { return brakeOilChangedDate; }
    public String getAirconFilterChangedDate() { return airconFilterChangedDate; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    public String getTempInfo() {
        return carModel+engineOilDate+batteryDate;
    }
}
