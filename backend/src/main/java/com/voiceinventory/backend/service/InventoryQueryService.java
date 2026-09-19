package com.voiceinventory.backend.service;

import com.voiceinventory.backend.dto.InventoryQueryResponse;
import com.voiceinventory.backend.entity.Product;
import com.voiceinventory.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryQueryService {

    private final ProductRepository productRepository;

    public InventoryQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public InventoryQueryResponse processQuestion(String question) {

        if (question == null || question.trim().isEmpty()) {
            return response(question, "Please ask a stock question.");
        }

        String input = question.toLowerCase().trim();

        List<Product> products = productRepository.findAll();

        // LOW STOCK
        if (input.contains("low") ||
            input.contains("shortage") ||
            input.contains("running out")) {

            List<String> lowStockItems = new ArrayList<>();

            for (Product product : products) {
                if (product.getQuantity()
                        .compareTo(product.getMinimumStock()) < 0) {

                    lowStockItems.add(
                            product.getName()
                                    + " - "
                                    + product.getQuantity()
                                    + " "
                                    + product.getUnit()
                                    + " (minimum "
                                    + product.getMinimumStock()
                                    + ")"
                    );
                }
            }

            if (lowStockItems.isEmpty()) {
                return new InventoryQueryResponse(
                        question,
                        "No products are currently low in stock.",
                        lowStockItems
                );
            }

            return new InventoryQueryResponse(
                    question,
                    "These products are currently low in stock.",
                    lowStockItems
            );
        }

        // REORDER
        if (input.contains("reorder") ||
            input.contains("restock")) {

            List<String> reorderItems = new ArrayList<>();

            for (Product product : products) {

                if (product.getQuantity()
                        .compareTo(product.getMinimumStock()) < 0) {

                    String reorder = product.getMinimumStock()
                            .subtract(product.getQuantity())
                            .toString();

                    reorderItems.add(
                            product.getName()
                                    + " - current: "
                                    + product.getQuantity()
                                    + " "
                                    + product.getUnit()
                                    + ", suggested reorder: "
                                    + reorder
                                    + " "
                                    + product.getUnit()
                    );
                }
            }

            if (reorderItems.isEmpty()) {
                return new InventoryQueryResponse(
                        question,
                        "No products need to be reordered.",
                        reorderItems
                );
            }

            return new InventoryQueryResponse(
                    question,
                    "These products may need to be reordered.",
                    reorderItems
            );
        }

        // TOTAL PRODUCTS
        if (input.contains("how many products") ||
            input.contains("total products")) {

            return response(
                    question,
                    "You have "
                            + productRepository.count()
                            + " products in inventory."
            );
        }

        // SHOW ALL PRODUCTS
        if (input.contains("show all") ||
            input.contains("all products") ||
            input.contains("list products") ||
            input.contains("list all")) {

            List<String> items = new ArrayList<>();

            for (Product product : products) {
                items.add(
                        product.getName()
                                + " - "
                                + product.getQuantity()
                                + " "
                                + product.getUnit()
                                + " - ₹"
                                + product.getPrice()
                );
            }

            return new InventoryQueryResponse(
                    question,
                    "Here is your current inventory.",
                    items
            );
        }

        /*
         * IMPORTANT:
         * Find the product BEFORE handling price/stock questions.
         * This prevents "eggs" from accidentally returning Rice.
         */
        Product product = findProduct(input, products);

        if (product == null) {
            return response(
                    question,
                    "I could not find that product in your inventory."
            );
        }

        String productName = product.getName();
        String unit = product.getUnit();

        // PRICE
        if (input.contains("price") ||
            input.contains("cost") ||
            input.contains("how much does")) {

            return response(
                    question,
                    "The price of "
                            + productName
                            + " is ₹"
                            + product.getPrice()
                            + " per "
                            + unit
                            + "."
            );
        }

        // MINIMUM STOCK
        if (input.contains("minimum") ||
            input.contains("minimum stock")) {

            return response(
                    question,
                    productName
                            + " has a minimum stock level of "
                            + product.getMinimumStock()
                            + " "
                            + unit
                            + "."
            );
        }

        // AVAILABLE STOCK
        if (input.contains("available") ||
            input.contains("stock") ||
            input.contains("quantity") ||
            input.contains("how many")) {

            return response(
                    question,
                    "Yes. You have "
                            + product.getQuantity()
                            + " "
                            + unit
                            + " of "
                            + productName
                            + " available."
            );
        }

        // PRODUCT INFORMATION
        if (input.contains("about") ||
            input.contains("information") ||
            input.contains("details") ||
            input.contains("tell me")) {

            return response(
                    question,
                    productName
                            + " has "
                            + product.getQuantity()
                            + " "
                            + unit
                            + " in stock. "
                            + "Price is ₹"
                            + product.getPrice()
                            + " per "
                            + unit
                            + ". "
                            + "Minimum stock is "
                            + product.getMinimumStock()
                            + " "
                            + unit
                            + "."
            );
        }

        return response(
                question,
                productName
                        + " has "
                        + product.getQuantity()
                        + " "
                        + unit
                        + " currently in stock."
        );
    }

    /*
     * Finds the product mentioned in the question.
     */
    private Product findProduct(
            String input,
            List<Product> products) {

        Product matchedProduct = null;

        for (Product product : products) {

            String productName =
                    product.getName().toLowerCase().trim();

            if (input.contains(productName)) {

                if (matchedProduct == null ||
                    productName.length()
                            > matchedProduct.getName()
                            .toLowerCase()
                            .length()) {

                    matchedProduct = product;
                }
            }
        }

        return matchedProduct;
    }

    private InventoryQueryResponse response(
            String question,
            String answer) {

        return new InventoryQueryResponse(
                question,
                answer,
                new ArrayList<>()
        );
    }
}
