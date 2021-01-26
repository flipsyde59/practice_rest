package com.practice.job.practice_rest.service.user;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.model.Role;
import com.practice.job.practice_rest.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param(value = "login") String login);
}
