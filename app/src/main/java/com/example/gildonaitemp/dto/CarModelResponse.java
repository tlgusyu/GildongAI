package com.example.gildonaitemp.dto;

public class CarModelResponse {

    private String id;
    private String manufacturer;
    private String modelName;
    private String modelYear;
    private String fuelType;
    private String bodyType;
    private String manualUrl;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getModelYear() { return modelYear; }
    public void setModelYear(String modelYear) { this.modelYear = modelYear; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public String getManualUrl() { return manualUrl; }
    public void setManualUrl(String manualUrl) { this.manualUrl = manualUrl; }
}
