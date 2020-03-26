package com.jsonprocessing.repositories;

import com.jsonprocessing.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal minPrice, BigDecimal maxPrice);
    Set<Product> findAllBySellerFirstNameAndSellerLastNameAndBuyerNotNull(String firstName, String lastName);

    Product findByName(String name);
}