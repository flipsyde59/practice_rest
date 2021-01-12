package com.practice.job.practice_rest.service;

import com.practice.job.practice_rest.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {

}
