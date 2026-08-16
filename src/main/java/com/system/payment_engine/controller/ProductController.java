package com.system.payment_engine.controller;

import com.system.payment_engine.dto.CreateProductDTO;
import com.system.payment_engine.model.Product;
import com.system.payment_engine.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductDTO dto) {
        Product product = Product.builder()
                .name(dto.name())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}