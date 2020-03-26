package com.jsonprocessing.services.implemetations;

import com.jsonprocessing.models.dtos.ProductAndSellerDto;
import com.jsonprocessing.models.dtos.ProductSeedDto;
import com.jsonprocessing.models.dtos.ProductWithBuyerFullNameDto;
import com.jsonprocessing.models.entities.Category;
import com.jsonprocessing.models.entities.Product;
import com.jsonprocessing.repositories.ProductRepository;
import com.jsonprocessing.services.CategoryService;
import com.jsonprocessing.services.ProductService;
import com.jsonprocessing.services.UserService;
import com.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedProducts(ProductSeedDto[] productSeedDtos) {
        if (this.productRepository.count() != 0) {
            System.out.println("This product file is already seeded");
        } else {
            Arrays.stream(productSeedDtos).forEach(productSeedDto -> {
                if (validationUtil.isValid(productSeedDto)) {
                    Product product = this.modelMapper
                            .map(productSeedDto, Product.class);
                    //Setting random seller @NonNull
                    product.setSeller(this.userService.getRandomUser());

                    Random random = new Random();
                    int randomNum = random.nextInt(2);
                    if (randomNum == 1) {
                        product.setBuyer(this.userService.getRandomUser());
                    }
                    product.setCategories(new HashSet<>(this.categoryService.getRandomCategory()));

                    this.productRepository.saveAndFlush(product);

                } else {
                    this.validationUtil.getViolations(productSeedDto).stream()
                            .map(ConstraintViolation::getMessage)
                            .forEach(System.out::println);
                }

            });
        }
    }

    @Override
    public List<ProductAndSellerDto> getAllProductsInRange(BigDecimal minPrice, BigDecimal maxPrice) {

        //Change price range
        minPrice = BigDecimal.valueOf(500);
        maxPrice = BigDecimal.valueOf(1000);
        return this.productRepository.findAllByPriceBetweenAndBuyerIsNull(minPrice, maxPrice)
                .stream()
                .map(product -> {
                    ProductAndSellerDto productAndSellerDto = this.modelMapper.map(product, ProductAndSellerDto.class);
                    productAndSellerDto
                            .setSeller(String.format("%s %s",
                                    product.getSeller().getFirstName(),
                                    product.getSeller().getLastName()
                            ));

                    return productAndSellerDto;
                })
                .collect(Collectors.toList());


    }

    @Override
    public Set<ProductWithBuyerFullNameDto> getAllProductsWithSellerFullName(String firstName, String lastName) {
        Set<ProductWithBuyerFullNameDto> dtos = new HashSet<>();
        this.productRepository.findAllBySellerFirstNameAndSellerLastNameAndBuyerNotNull(firstName, lastName).forEach(product -> {
            dtos.add(this.modelMapper.map(product, ProductWithBuyerFullNameDto.class));
        });
        return dtos;
    }


}
