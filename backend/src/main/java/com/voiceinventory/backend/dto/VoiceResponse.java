package com.voiceinventory.backend.dto;

import java.math.BigDecimal;

public class VoiceResponse {

    private String originalText;
    private String action;
    private String productName;
    private BigDecimal quantity;
    private String unit;
    private String message;

    public VoiceResponse() {
    }

    public VoiceResponse(String originalText,
                         String action,
                         String productName,
                         BigDecimal quantity,
                         String unit,
                         String message) {
        this.originalText = originalText;
        this.action = action;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.message = message;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
