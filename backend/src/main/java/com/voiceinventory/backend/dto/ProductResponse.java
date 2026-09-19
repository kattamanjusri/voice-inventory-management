package com.voiceinventory.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.voiceinventory.backend.entity.Product;

// What we send back. "lowStock" is calculated, never stored:
// a product is low when quantity < minimumStock.
public class ProductResponse {

    private Long id;
    private String name;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal minimumStock;
    private BigDecimal price;
    private boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse from(Product p) {
        ProductResponse r = new ProductResponse();
        r.id = p.getId();
        r.name = p.getName();
        r.unit = p.getUnit();
        r.quantity = p.getQuantity();
        r.minimumStock = p.getMinimumStock();
        r.price = p.getPrice();
        r.lowStock = p.getQuantity().compareTo(p.getMinimumStock()) < 0;
        r.createdAt = p.getCreatedAt();
        r.updatedAt = p.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getMinimumStock() { return minimumStock; }
    public BigDecimal getPrice() { return price; }
    public boolean isLowStock() { return lowStock; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
