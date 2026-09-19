package com.voiceinventory.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// A DTO (Data Transfer Object) is the shape of the JSON a client sends.
// The annotations are our validation rules.
public class ProductRequest {

    @NotBlank(message = "Product name cannot be empty.")
    @Size(max = 100, message = "Product name is too long.")
    private String name;

    @NotBlank(message = "Please choose a unit.")
    private String unit;

    @NotNull(message = "Please enter the current quantity.")
    @DecimalMin(value = "0", message = "Quantity cannot be negative.")
    private BigDecimal quantity;

    @NotNull(message = "Please enter the minimum stock level.")
    @DecimalMin(value = "0", message = "Minimum stock cannot be negative.")
    private BigDecimal minimumStock;

    @DecimalMin(value = "0", message = "Price cannot be negative.")
    private BigDecimal price;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getMinimumStock() { return minimumStock; }
    public void setMinimumStock(BigDecimal minimumStock) { this.minimumStock = minimumStock; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
