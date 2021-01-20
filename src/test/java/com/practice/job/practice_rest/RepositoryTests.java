package com.practice.job.practice_rest;
import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RepositoryTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRepository repository;

    @Test
    public void simpleTest() throws Exception {
        mvc.perform(get("/clients/"))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.name").value("Oleg"))
                //andExpect(jsonPath("$.email").value("oleg1111@psu.ru"));
    }

    @Test
    void getEmptyDBTest(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/clients/")).andExpect(status().isOk()).andExpect(content().string("No clients in database"));
    }

//    @Test
//    void postManyTest(@Autowired MockMvc mvc) throws Exception {
//        mvc.perform(post("/clients/addMany"));
//    }

    @Test
    void getNotEmptyDBTest(@Autowired MockMvc mvc) throws Exception{
        mvc.perform(get("/clients/")).andExpect(status().isOk()).andExpect(content().json("[{\"id\":1,\"name\": \"Artyom\",\"email\": \"arti12354@psu.ru\",\"age\": 19,\"educated\": \"false\",\"birth_date\": \"03.08.2001\",\"growth\": 1.83},{{\"id\":1,\"name\": \"Ilya\",\"email\": \"ilyusha55@psu.ru\",\"age\": 32,\"educated\": \"true\",\"birth_date\": \"15.12.1988\",\"growth\": 1.73}]"));
    }

}