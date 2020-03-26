package com.jsonprocessing.services;

import com.jsonprocessing.models.dtos.ProductAndSellerDto;
import com.jsonprocessing.models.dtos.ProductSeedDto;
import com.jsonprocessing.models.dtos.ProductWithBuyerFullNameDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService  {
    void seedProducts(ProductSeedDto[] productSeedDtos);
    List<ProductAndSellerDto> getAllProductsInRange(BigDecimal minPrice, BigDecimal maxPrice);
    Set<ProductWithBuyerFullNameDto> getAllProductsWithSellerFullName(String firstName, String lastName);

}
