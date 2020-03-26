package com.jsonprocessing.controllers;

import com.google.gson.Gson;
import com.jsonprocessing.models.dtos.*;
import com.jsonprocessing.services.CategoryService;
import com.jsonprocessing.services.ProductService;
import com.jsonprocessing.services.UserService;
import com.jsonprocessing.utils.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


import static com.jsonprocessing.constants.GlobalConstants.*;

@Component
public class AppController implements CommandLineRunner {
    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final FileIOUtil fileIOUtil;

    @Autowired
    public AppController(Gson gson, CategoryService categoryService, UserService userService, ProductService productService, FileIOUtil fileIOUtil) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.fileIOUtil = fileIOUtil;
    }

    @Override
    public void run(String... args) throws Exception {
//        this.seedCategories();
//        this.seedUsers();
//        this.seedProducts();
//        writeProductsInRange();
//        writeUsersWithSoldProducts();
//        writeCategoriesByProducts();
    }

    private void writeCategoriesByProducts() throws IOException {
        List<CategoryByProductsDto> categoriesByProducts = this.categoryService.getCategoriesByProducts();

        String json = this.gson.toJson(categoriesByProducts);

        this.fileIOUtil.write(json, CATEGORIES_BY_PRODUCTS);
    }

    private void writeUsersWithSoldProducts() throws IOException {
        LinkedList<UserWithSoldProductDto> userWithSoldProductDtos = new LinkedList<>(this.userService.usersWithSoldProducts());
        userWithSoldProductDtos.forEach(userWithSoldProductDto -> {
            userWithSoldProductDto.setSoldProducts(this.productService.getAllProductsWithSellerFullName(
                    userWithSoldProductDto.getFirstName(),
                    userWithSoldProductDto.getLastName()
            ));
        });
        String json = this.gson.toJson(userWithSoldProductDtos);
        this.fileIOUtil.write(json, USERS_WITH_SOLD_PRODUCTS);
    }


    private void writeProductsInRange() throws IOException {

        //Change values in productService class before use;
        List<ProductAndSellerDto> productAndSellerDtosList =
                this.productService.getAllProductsInRange(BigDecimal.ONE, BigDecimal.ZERO);

        String json = this.gson.toJson(productAndSellerDtosList);

        this.fileIOUtil.write(json, PRODUCT_WITH_SELLER_OUTPUT_FILE_PATH);

    }

    private void seedProducts() throws FileNotFoundException {
        ProductSeedDto[] productSeedDtos = this.gson
                .fromJson(new FileReader(PRODUCTS_FILE_PATH),
                        ProductSeedDto[].class
                );

        this.productService.seedProducts(productSeedDtos);
    }

    private void seedUsers() throws FileNotFoundException {
        UserSeedDto[] userSeedDtos = this.gson
                .fromJson(new FileReader(USERS_FILE_PATH),
                        UserSeedDto[].class
                );

        this.userService.seedUsers(userSeedDtos);
    }

    private void seedCategories() throws FileNotFoundException {
        CategorySeedDto[] dtos = this.gson
                .fromJson(new FileReader(CATEGORIES_FILE_PATH),
                        CategorySeedDto[].class);


        this.categoryService.seedCategories(dtos);
    }


}
