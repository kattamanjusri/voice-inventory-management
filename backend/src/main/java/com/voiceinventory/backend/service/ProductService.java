package com.voiceinventory.backend.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voiceinventory.backend.dto.ProductRequest;
import com.voiceinventory.backend.dto.ProductResponse;
import com.voiceinventory.backend.entity.Product;
import com.voiceinventory.backend.exception.BadRequestException;
import com.voiceinventory.backend.exception.NotFoundException;
import com.voiceinventory.backend.repository.ProductRepository;

// @Service: this class holds the business rules.
// @Transactional: if anything fails halfway, the database change is undone.
@Service
public class ProductService {

    // The 8 trade units from the requirements.
    public static final Set<String> VALID_UNITS =
            Set.of("pieces", "kg", "bags", "cartons", "boxes", "dozens", "litres", "quintals");

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list(String search) {
        String text = (search == null) ? "" : search.trim();
        return productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(text)
                .stream().map(ProductResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        return ProductResponse.from(findOrThrow(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        String name = request.getName().trim();
        String unit = normalizeUnit(request.getUnit());

        if (productRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("A product named \"" + name + "\" already exists.");
        }

        Product product = new Product();
        applyRequest(product, name, unit, request);
        return ProductResponse.from(productRepository.saveAndFlush(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findOrThrow(id);
        String name = request.getName().trim();
        String unit = normalizeUnit(request.getUnit());

        if (productRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new BadRequestException("Another product is already named \"" + name + "\".");
        }

        applyRequest(product, name, unit, request);
        return ProductResponse.from(productRepository.saveAndFlush(product));
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(findOrThrow(id));
    }

    // ---------- helpers ----------

    private void applyRequest(Product product, String name, String unit, ProductRequest request) {
        product.setName(name);
        product.setUnit(unit);
        product.setQuantity(request.getQuantity());
        product.setMinimumStock(request.getMinimumStock());
        product.setPrice(request.getPrice());
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found."));
    }

    // Accepts "Bags", " KG " and so on, and stores the clean lowercase unit.
    private String normalizeUnit(String unit) {
        String clean = unit.trim().toLowerCase();
        if (!VALID_UNITS.contains(clean)) {
            throw new BadRequestException("\"" + unit
                    + "\" is not a valid unit. Please choose one of: pieces, kg, bags, cartons, boxes, dozens, litres, quintals.");
        }
        return clean;
    }
}
