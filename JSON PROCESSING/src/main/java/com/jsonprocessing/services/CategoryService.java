package com.jsonprocessing.services;

import com.jsonprocessing.models.dtos.CategoryByProductsDto;
import com.jsonprocessing.models.dtos.CategorySeedDto;
import com.jsonprocessing.models.entities.Category;

import java.util.List;


public interface CategoryService {
   void seedCategories(CategorySeedDto[] categorySeedDtos);
   List<Category> getRandomCategory();
   List<CategoryByProductsDto> getCategoriesByProducts();
}
