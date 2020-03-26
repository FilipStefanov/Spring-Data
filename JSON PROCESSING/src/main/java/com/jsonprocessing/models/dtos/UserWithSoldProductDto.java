package com.jsonprocessing.models.dtos;

import com.google.gson.annotations.Expose;
import com.jsonprocessing.models.entities.Product;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

import java.util.Set;

public class UserWithSoldProductDto {
    @Expose
    private String firstName;
    @Expose
    @Length(min = 3, message = "Last name: wrong length")
    private String lastName;
    @Expose
    private Set<ProductWithBuyerFullNameDto> soldProducts;

    public UserWithSoldProductDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ProductWithBuyerFullNameDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<ProductWithBuyerFullNameDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
