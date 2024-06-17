package com.example.courseproject.presentation.config;

import com.example.courseproject.data.database.DiscountDataBase;
import com.example.courseproject.data.repository.DiscRepository;
import com.example.courseproject.data.repository.ProdRepository;
import com.example.courseproject.domain.port.DiscountRepository;
import com.example.courseproject.domain.port.ProductRepository;
import com.example.courseproject.domain.usecase.Discount.GetDiscountAmountUseCase;
import com.example.courseproject.domain.usecase.Discount.GetDiscountPriceUseCase;
import com.example.courseproject.domain.usecase.Discount.GetDiscountTypeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductBarcodeUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductCountUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductNameUseCase;
import com.example.courseproject.domain.usecase.Product.GetProductPriceUseCase;

public class DefaultConfig {

    public static DefaultConfig instanse;
    private final DiscountRepository discountRepository = DiscRepository.getInstance();
    private final ProductRepository productRepository = ProdRepository.getInstance();

    public static DefaultConfig getInstance() {
        if (instanse == null) {
            instanse = new DefaultConfig();
        }
        return instanse;
    }

    public GetProductBarcodeUseCase getProductBarcodeUseCase()
    { return new GetProductBarcodeUseCase(productRepository);}

    public GetProductNameUseCase getProductNameUseCase()
    { return new GetProductNameUseCase(productRepository);}

    public GetProductCountUseCase getProductCountUseCase()
    { return new GetProductCountUseCase(productRepository);}

    public GetProductPriceUseCase getProductPriceUseCase()
    { return new GetProductPriceUseCase(productRepository);}


    public GetDiscountAmountUseCase getDiscountAmountUseCase()
    { return new GetDiscountAmountUseCase(discountRepository);}

    public GetDiscountPriceUseCase getDiscountPriceUseCase()
    { return new GetDiscountPriceUseCase(discountRepository);}

    public GetDiscountTypeUseCase getDiscountTypeUseCase()
    { return new GetDiscountTypeUseCase(discountRepository);}

}
