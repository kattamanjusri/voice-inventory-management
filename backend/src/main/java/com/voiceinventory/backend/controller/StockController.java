package com.voiceinventory.backend.controller;

import com.voiceinventory.backend.dto.StockUpdateRequest;
import com.voiceinventory.backend.entity.Product;
import com.voiceinventory.backend.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/{id}/add")
    public ResponseEntity<Product> addStock(
            @PathVariable Long id,
            @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                stockService.addStock(id, request)
        );
    }

    @PostMapping("/{id}/remove")
    public ResponseEntity<Product> removeStock(
            @PathVariable Long id,
            @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                stockService.removeStock(id, request)
        );
    }
}
