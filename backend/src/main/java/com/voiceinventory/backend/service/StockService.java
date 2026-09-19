package com.voiceinventory.backend.service;

import com.voiceinventory.backend.dto.StockUpdateRequest;
import com.voiceinventory.backend.entity.Product;
import com.voiceinventory.backend.entity.StockTransaction;
import com.voiceinventory.backend.exception.BadRequestException;
import com.voiceinventory.backend.exception.NotFoundException;
import com.voiceinventory.backend.repository.ProductRepository;
import com.voiceinventory.backend.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;

    public StockService(ProductRepository productRepository,
                        StockTransactionRepository stockTransactionRepository) {
        this.productRepository = productRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    public Product addStock(Long productId, StockUpdateRequest request) {

        validateQuantity(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        BigDecimal newQuantity =
                product.getQuantity().add(request.getQuantity());

        product.setQuantity(newQuantity);

        Product savedProduct = productRepository.save(product);

        saveTransaction(product, request.getQuantity(), "ADD");

        return savedProduct;
    }

    public Product removeStock(Long productId, StockUpdateRequest request) {

        validateQuantity(request);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found."));

        if (product.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new BadRequestException("Not enough stock available.");
        }

        BigDecimal newQuantity =
                product.getQuantity().subtract(request.getQuantity());

        product.setQuantity(newQuantity);

        Product savedProduct = productRepository.save(product);

        saveTransaction(product, request.getQuantity(), "REMOVE");

        return savedProduct;
    }

    private void validateQuantity(StockUpdateRequest request) {

        if (request == null || request.getQuantity() == null) {
            throw new BadRequestException("Quantity is required.");
        }

        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Quantity must be greater than 0.");
        }
    }

    private void saveTransaction(Product product,
                                  BigDecimal quantity,
                                  String action) {

        StockTransaction transaction = new StockTransaction();

        transaction.setProduct(product);
        transaction.setAction(action);
        transaction.setQuantity(quantity);
        transaction.setUnit(product.getUnit());

        stockTransactionRepository.save(transaction);
    }
}
