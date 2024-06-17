package com.example.courseproject.domain.usecase.Product;

import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.port.ProductRepository;

public class GetProductCountUseCase {
    private final ProductRepository productRepository;

    public GetProductCountUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String invoke(Product product){
        return this.productRepository.getProductCount(product);
    }
}
