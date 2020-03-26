package com.jsonprocessing.repositories;

import com.jsonprocessing.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT avg(p.price)\n" +
            "from categories as c\n" +
            "         inner join products_categories pc on c.id = pc.categories_id\n" +
            "         inner join products p on pc.products_id = p.id\n" +
            "WHERE c.name like :name\n" +
            ";",
            nativeQuery = true)
    BigDecimal findAveragePriceByName(@Param("name") String name);

    @Query(value = "SELECT sum(p.price)\n" +
            "from categories as c\n" +
            "         inner join products_categories pc on c.id = pc.categories_id\n" +
            "         inner join products p on pc.products_id = p.id\n" +
            "WHERE c.name like :name\n" +
            ";",
            nativeQuery = true)
    BigDecimal findPriceSumOfAllProductsByCategory(@Param("name") String name);

    List<Category> getAllByProductsNotNull();

    Category getCategoryByName(String name);

    List<Category> findAllByIdNotNull();
}