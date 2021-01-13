package com.practice.job.practice_rest.service;

import com.practice.job.practice_rest.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    Client findById(long id);

    @Modifying
    @Transactional
    @Query("update Client c set c.name = :name where c.id = :id")
    void updateName(@Param(value = "id") Integer id, @Param(value = "name") String name);

    @Modifying
    @Transactional
    @Query("update Client c set c.email = :email where c.id = :id")
    void updateEmail(@Param(value = "id") Integer id, @Param(value = "email") String email);

    @Modifying
    @Transactional
    @Query("update Client c set c.age = :age where c.id = :id")
    void updateAge(@Param(value = "id") Integer id, @Param(value = "age") Integer age);

    @Modifying
    @Transactional
    @Query("update Client c set c.educated = :educated where c.id = :id")
    void updateEducated(@Param(value = "id") Integer id, @Param(value = "educated") Boolean educated);

    @Modifying
    @Transactional
    @Query("update Client c set c.birth_date = :birth_date where c.id = :id")
    void updateBirth_date(@Param(value = "id") Integer id, @Param(value = "birth_date") Date birth_date);

    @Modifying
    @Transactional
    @Query("update Client c set c.growth = :growth where c.id = :id")
    void updateGrowth(@Param(value = "id") Integer id, @Param(value = "growth") Float growth);
}
