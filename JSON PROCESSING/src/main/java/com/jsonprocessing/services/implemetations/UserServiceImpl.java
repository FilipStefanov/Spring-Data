package com.jsonprocessing.services.implemetations;

import com.jsonprocessing.models.dtos.UserSeedDto;
import com.jsonprocessing.models.dtos.UserWithSoldProductDto;
import com.jsonprocessing.models.entities.User;
import com.jsonprocessing.repositories.UserRepository;
import com.jsonprocessing.services.ProductService;
import com.jsonprocessing.services.UserService;
import com.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;

    }

    @Override
    public void seedUsers(UserSeedDto[] userSeedDtos) {
        if (this.userRepository.count() != 0) {
            System.out.println("This file is already inserted!");
            return;
        }
        Arrays.stream(userSeedDtos).forEach(userSeedDto -> {
            if (this.validationUtil.isValid(userSeedDto)) {
                User user = this.modelMapper
                        .map(userSeedDto, User.class);
                if (this.userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()) == user) {
                    System.out.println(String.format("%s %s already exist in the database%n",
                            user.getFirstName(), user.getLastName()
                    ));
                } else {
                    this.userRepository.saveAndFlush(user);
                }

            } else {
                this.validationUtil.getViolations(userSeedDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }

        });
    }

    @Override
    public User getRandomUser() {
        Random random = new Random();
        long randomId = random.nextInt((int) this.userRepository.count()) + 1;
        return this.userRepository.getOne(randomId);
    }

    @Override
    public Set<UserWithSoldProductDto> usersWithSoldProducts() {
        List<User> users = this.userRepository.findDistinctBySoldNotNullOrderByLastNameAscFirstNameAsc();

        //Using linkedHashSet to preserve the order in which the list is extracted from the DB
        Set<UserWithSoldProductDto> userWithSoldProductDtos = new LinkedHashSet<>();

        users.forEach(user -> {
            userWithSoldProductDtos.add(this.modelMapper.map(user, UserWithSoldProductDto.class));
        });
        return userWithSoldProductDtos;
    }


}
