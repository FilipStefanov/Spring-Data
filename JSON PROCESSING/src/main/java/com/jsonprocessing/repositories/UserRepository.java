package com.jsonprocessing.repositories;

import com.jsonprocessing.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     LinkedList<User> findDistinctBySoldNotNullOrderByLastNameAscFirstNameAsc();
      User findByFirstNameAndLastName(String first, String last);

}
