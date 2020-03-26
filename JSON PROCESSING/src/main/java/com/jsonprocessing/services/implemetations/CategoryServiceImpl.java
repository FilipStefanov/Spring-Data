package com.jsonprocessing.services.implemetations;

import com.jsonprocessing.models.dtos.CategoryByProductsDto;
import com.jsonprocessing.models.dtos.CategorySeedDto;
import com.jsonprocessing.models.entities.Category;
import com.jsonprocessing.repositories.CategoryRepository;
import com.jsonprocessing.services.CategoryService;
import com.jsonprocessing.services.ProductService;
import com.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;

    }

    @Override
    public void seedCategories(CategorySeedDto[] categorySeedDtos) {
        if (this.categoryRepository.count() != 0) {
            return;
        }
        Arrays.stream(categorySeedDtos)
                .forEach(categorySeedDto -> {
                    if (this.validationUtil.isValid(categorySeedDto)) {
                        Category category = this.modelMapper.map(categorySeedDto, Category.class);
                        this.categoryRepository.saveAndFlush(category);

                    } else {
                        this.validationUtil.getViolations(categorySeedDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });


    }

    @Override
    public List<Category> getRandomCategory() {
        Random random = new Random();
        List<Category> resultList = new ArrayList<>();
        int randomCounter = random.nextInt(3) + 1;
        for (int i = 0; i < randomCounter; i++) {
            long randomId = random.nextInt((int) this.categoryRepository.count()) + 1;
            resultList.add(this.categoryRepository.getOne(randomId));
        }
        return resultList;
    }

    @Override
    public List<CategoryByProductsDto> getCategoriesByProducts() {

        LinkedHashSet<CategoryByProductsDto> categoryByProductsDtos = new LinkedHashSet<>();

        this.categoryRepository.findAllByIdNotNull().forEach(category -> {
            CategoryByProductsDto categoryByProductsDto = (this.modelMapper.map(category, CategoryByProductsDto.class));
            categoryByProductsDto.
                    setProductsCount(
                            this.categoryRepository
                                    .getCategoryByName(categoryByProductsDto.getName())
                                    .getProducts().size());
            categoryByProductsDto.setAveragePrice(
                    this.categoryRepository
                            .findAveragePriceByName(
                                    categoryByProductsDto.getName()));
            categoryByProductsDto.setTotalRevenue(
                    this.categoryRepository
                            .findPriceSumOfAllProductsByCategory(
                                    categoryByProductsDto.getName())

            );
            categoryByProductsDtos.add(categoryByProductsDto);
        });

        return new ArrayList<>(categoryByProductsDtos);
    }
}
