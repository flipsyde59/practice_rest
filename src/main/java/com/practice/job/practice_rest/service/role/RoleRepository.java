package com.practice.job.practice_rest.service.role;

import com.practice.job.practice_rest.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    //@Transactional
    @Query("select r from Role r where r.type = :type")
    Role findByType(@Param(value = "type") String type);
}
