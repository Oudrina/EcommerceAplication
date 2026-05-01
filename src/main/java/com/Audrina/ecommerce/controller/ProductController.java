package com.Audrina.ecommerce.controller;

import com.Audrina.ecommerce.dto.ProductRequest;
import com.Audrina.ecommerce.dto.ProductResponse;
import com.Audrina.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    @GetMapping("active")
    public ResponseEntity<List<ProductResponse>> findAllActiveProducts() {
        return ResponseEntity.ok(productService.findByActiveTrue());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return productService.findById(id).map(
                product ->
                        new ResponseEntity<>(product, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        productService.saveProduct(productRequest);
        return ResponseEntity.ok("Product created successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        boolean updateProduct = productService.updateProduct(id, productRequest);
        if (updateProduct)
            return ResponseEntity.ok("Product updated successfully");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProduct(@RequestParam String keyword) {
        return productService.searchProducts(keyword);

    }
}
