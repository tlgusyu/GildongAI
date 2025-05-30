package com.example.gildonaitemp.dto;

import java.util.List;

public class ConsumableOverviewResponse {private List<ConsumableResponse> all;
    private String nextDueDate;

    public List<ConsumableResponse> getAll() { return all; }
    public String getNextDueDate() { return nextDueDate; }
}
