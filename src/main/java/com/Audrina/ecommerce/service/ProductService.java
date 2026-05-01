package com.Audrina.ecommerce.service;

import com.Audrina.ecommerce.dto.ProductRequest;
import com.Audrina.ecommerce.dto.ProductResponse;
import com.Audrina.ecommerce.model.Product;
import com.Audrina.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(
                        this::mapToProductResponse
                ).toList();
    }

    public Optional<ProductResponse> findById(Long id) {
        return productRepository.findById(id).stream()
                .findFirst()
                .map(this::mapToProductResponse);
    }

    public void saveProduct(ProductRequest productRequest) {
        Product product = new Product();
        ProductFromProductRequest(product, productRequest);
        productRepository.save(product);
    }


    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(
                product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }
        ).orElse(false);
    }

    public Boolean updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(
                existingProduct -> {
                    ProductFromProductRequest(existingProduct, productRequest);
                    productRepository.save(existingProduct);
                    return true;
                }
        ).orElse(false);

    }


    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream().map(
                this::mapToProductResponse
        ).toList();
    }

    public List<ProductResponse> findByActiveTrue() {
        return productRepository.findAllByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private void ProductFromProductRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setDescription(productRequest.getDescription());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setActive(true);
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.getActive());
        response.setStockQuantity(product.getStockQuantity());
        response.setCreatedAt(product.getCreatedAt());
        return response;
    }

}
