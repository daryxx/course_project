package com.example.courseproject.domain.usecase.Product;

import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.port.ProductRepository;

import java.util.List;

public class GetProductNameUseCase {

    private final ProductRepository productRepository;
    public GetProductNameUseCase (ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public String invoke(Product product){
        return this.productRepository.getProductName(product);
    }

}
