package com.example.courseproject.domain.usecase.Discount;

import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.port.DiscountRepository;

public class GetDiscountTypeUseCase {

    private final DiscountRepository discountRepository;

    public GetDiscountTypeUseCase(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public String invoke(Discount discount){
        return this.discountRepository.getDiscountType(discount);
    }

}
