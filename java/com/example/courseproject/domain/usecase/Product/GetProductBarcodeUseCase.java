package com.example.courseproject.domain.usecase.Product;

import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.port.ProductRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GetProductBarcodeUseCase {

    private final ProductRepository productRepository;

    public GetProductBarcodeUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String invoke(Product product){
        return this.productRepository.getProductBarcode(product);
    }

}
