package com.voiceinventory.backend.dto;

import java.math.BigDecimal;

public class StockUpdateRequest {

    private BigDecimal quantity;
    private String unit;

    public StockUpdateRequest() {
    }

    public StockUpdateRequest(BigDecimal quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
