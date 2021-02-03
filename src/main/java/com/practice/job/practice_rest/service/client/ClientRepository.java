package com.practice.job.practice_rest.service.client;

import com.practice.job.practice_rest.model.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Modifying
    @Transactional
    @Query("update Client c set c.name = :name," +
           " c.email = :email, c.age = :age," +
           " c.educated = :educated," +
           " c.birth_date = :birth_date," +
           " c.growth = :growth" +
                " where c.id = :id")
    void update(@Param(value = "id") Integer id,
                @Param(value = "name") String name,
                @Param(value = "email") String email,
                @Param(value = "age") Integer age,
                @Param(value = "educated") Boolean educated,
                @Param(value = "birth_date") Date birth_date,
                @Param(value = "growth") Float growth
    );

}
