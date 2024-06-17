package com.example.courseproject.domain.port;

import com.example.courseproject.domain.entity.Discount;
import com.example.courseproject.domain.entity.Product;

import java.util.List;

public interface DiscountRepository {

    String getDiscountAmount(Discount discount);

    String getDiscountType(Discount discount);

    String getDiscountPrice(Discount discount);


}
