package com.voiceinventory.backend.controller;

import com.voiceinventory.backend.dto.InventoryQueryResponse;
import com.voiceinventory.backend.service.InventoryQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5176"
})
public class InventoryQueryController {

    private final InventoryQueryService inventoryQueryService;

    public InventoryQueryController(InventoryQueryService inventoryQueryService) {
        this.inventoryQueryService = inventoryQueryService;
    }

    @GetMapping("/query")
    public InventoryQueryResponse query(
            @RequestParam String question) {

        return inventoryQueryService.processQuestion(question);
    }
}
