package com.example.courseproject.data.repository;

import com.example.courseproject.data.database.ProductDataBase;
import com.example.courseproject.domain.entity.Product;
import com.example.courseproject.domain.port.ProductRepository;

import java.util.List;

public class ProdRepository implements ProductRepository {

    private static ProdRepository instanse;
    public ProdRepository(){

    }

    public static ProdRepository getInstance() {
        if (instanse == null) {
            instanse = new ProdRepository();
        }
        return instanse;
    }

    @Override
    public String getProductBarcode(Product product) {
        return product.getBarcode();
    }

    @Override
    public String getProductName(Product product) {
        return product.getName();
    }

    @Override
    public String getProductPrice(Product product) {
        return product.getPrice();
    }

    @Override
    public String getProductCount(Product product) {
        return product.getCount();
    }


}
