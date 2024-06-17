package com.example.courseproject.domain.port;

import com.example.courseproject.domain.entity.Product;

import java.util.List;

public interface ProductRepository {


    String getProductBarcode(/*int position*/Product product);

    String getProductName(/*int position*/Product product);

    String getProductPrice(Product product);

    String getProductCount(Product product);
}
