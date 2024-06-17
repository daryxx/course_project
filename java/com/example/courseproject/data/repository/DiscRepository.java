package com.example.courseproject.data.repository;

import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.port.DiscountRepository;

public class DiscRepository implements DiscountRepository {
    private static DiscRepository instanse;
    public DiscRepository(){

    }

    public static DiscRepository getInstance() {
        if (instanse == null) {
            instanse = new DiscRepository();
        }
        return instanse;
    }

    @Override
    public String getDiscountAmount(Discount discount) {
        return discount.getAmount();
    }

    @Override
    public String getDiscountType(Discount discount) {
        return discount.getType();
    }

    @Override
    public String getDiscountPrice(Discount discount) {
        return discount.getAfterPrice();
    }
}
