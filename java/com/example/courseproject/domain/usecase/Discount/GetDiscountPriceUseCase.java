package com.example.courseproject.domain.usecase.Discount;

import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.port.DiscountRepository;

public class GetDiscountPriceUseCase {

    private final DiscountRepository discountRepository;

    public GetDiscountPriceUseCase(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public String invoke(Discount discount){
        return this.discountRepository.getDiscountPrice(discount);
    }

}
