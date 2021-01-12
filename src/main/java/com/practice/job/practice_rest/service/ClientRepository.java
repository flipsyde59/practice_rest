package com.practice.job.practice_rest.service;

import com.practice.job.practice_rest.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    Client findById(long id);

    @Modifying
    @Query("update Client u set u.email = :email where u.id = :id")
    void updateEmail(@Param(value = "id") Integer id, @Param(value = "email") String email);
}
