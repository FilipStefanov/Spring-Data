package com.jsonprocessing.services;

import com.jsonprocessing.models.dtos.UserSeedDto;
import com.jsonprocessing.models.dtos.UserWithSoldProductDto;
import com.jsonprocessing.models.entities.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    void seedUsers(UserSeedDto[] userSeedDtos);
    User getRandomUser();
    Set<UserWithSoldProductDto> usersWithSoldProducts();

}
