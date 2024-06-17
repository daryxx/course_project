package com.example.courseproject.domain.usecase.Product;

import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.port.ProductRepository;

public class GetProductPriceUseCase {
    private final ProductRepository productRepository;

    public GetProductPriceUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String invoke(Product product){
        return this.productRepository.getProductPrice(product);
    }
}
