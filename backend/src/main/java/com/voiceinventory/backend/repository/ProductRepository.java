package com.voiceinventory.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voiceinventory.backend.entity.Product;

// @Repository: this interface talks to the database.
// JpaRepository already gives us save, findById, findAll, delete...
// Spring writes the SQL for the extra methods from their names.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String text);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
