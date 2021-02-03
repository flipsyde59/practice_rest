package com.practice.job.practice_rest;

import com.practice.job.practice_rest.service.client.ClientRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRepository repository;

    @Test
    void posting() throws Exception{
        mvc.perform(post("/rest/clients/addOnes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Ilya",
                        "email": "ilyusha55@psu.ru",
                        "age": 32,
                        "educated": "true",
                        "birth_date": "1988-12-15",
                        "growth": 1.73
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Client was added"));
    }
    @Test
    @Order(1)
    void getEmptyDBTest() throws Exception {
        mvc.perform(get("/rest/clients/"))
                .andExpect(status().isOk())
                .andExpect(content().string("No clients in database"));
    }

    @Test
    @Order(2)
    void postManyTest() throws Exception {
        mvc.perform(post("/rest/clients/addMany")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [{
                        "name":"Micael",
                        "email":"micael123@psu.ru",
                        "age":25,"educated":false,
                        "birth_date":"1995-12-05",
                        "growth":1.8
                        },
                        {
                        "name":"Kate",
                        "email":"katekitty@psu.ru",
                        "age":18,"educated":false,
                        "birth_date":"2002-05-19",
                        "growth":1.55
                        },
                        {
                        "name":"Oleg",
                        "email":"oleg1111@psu.ru",
                        "age":22,
                        "educated":true,
                        "birth_date":"1998-06-20",
                        "growth":1.95
                        }]""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("All clients were added"));
    }

    @Test
    @Order(3)
    void postOneTest() throws Exception {
        mvc.perform(post("/rest/clients/addOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Ilya",
                        "email": "ilyusha55@psu.ru",
                        "age": 32,
                        "educated": "true",
                        "birth_date": "1988-12-15",
                        "growth": 1.73
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Client was added"));
    }

    @Test
    @Order(4)
    void getNotEmptyDBTest() throws Exception {
        mvc.perform(get("/rest/clients/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(5)
    void deleteExistingClientById() throws Exception{
        mvc.perform(delete("/rest/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("The client have been deleted"));
    }

    @Test
    @Order(6)
    void deleteNotExistingClientById() throws Exception{
        mvc.perform(delete("/rest/clients/22"))
                .andExpect(status().isOk())
                .andExpect(content().string("Nothing have been changed"));
    }

    @Test
    @Order(7)
    void updateExistingClientById() throws Exception{
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyyy@psu.ru",
                        "age": 22,
                        "educated": "false",
                        "birth_date": "1998-12-14"
                        "growth": 1.63
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Fields that have been updated:\nname|email|age|educated|birth_date|growth"));
    }

    @Test
    @Order(8)
    void updateExistingClientByIdWithoutChange() throws Exception{
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyyy@psu.ru",
                        "age": 22,
                        "educated": "false",
                        "birth_date": "1998-12-14",
                        "growth": 1.63
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Nothing have been changed"));
    }

    @Test
    @Order(9)
    void updateNotExistingClientById() throws Exception{
        mvc.perform(put("/rest/clients/22")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyyy@psu.ru",
                        "age": 22,
                        "educated": "false",
                        "birth_date": "1998-12-14",
                        "growth": 1.63
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Client with that id=22 not found"));
    }

    @Test
    @Order(10)
    void updateClientByIdWithErrorsInData() throws Exception{
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyy.y@..psu@.ru",
                        "age": "2fff2",
                        "educated": "false",
                        "birth_date": "1--4.1--2.1-998",
                        "growth": "1.6vghj3"
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Error: Data can not be parsed\nEmail not valid\nExample: \"name@example.com\"\nError: Data can not be parsed\nAge must be an integer\nExample: 25\nError: Data can not be parsed\nDate must be the \"dd.MM.yyyy\" format\nExample: \"09.09.1999\"\nError: Data can not be parsed\nGrowth must be in meters and have a fractional value.\nExample: \"1.59\"\nUnforeseeable error. Incorrect data detected"));
    }

    @Test
    @Order(11)
    void postClientWithErrorsInData() throws Exception{
        mvc.perform(post("/rest/clients/addOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyy.y@..psu@.ru",
                        "age": "2fff2",
                        "educated": "false",
                        "birth_date": "1--4.1--2.1-998",
                        "growth": "1.6vghj3"
                        }""".stripIndent()))
                .andExpect(status().isOk())
                .andExpect(content().string("Error: Data can not be parsed\nEmail not valid\nExample: \"name@example.com\"\nError: Data can not be parsed\nAge must be an integer\nExample: 25\nError: Data can not be parsed\nDate must be the \"dd.MM.yyyy\" format\nExample: \"09.09.1999\"\nError: Data can not be parsed\nGrowth must be in meters and have a fractional value.\nExample: \"1.59\"\nUnforeseeable error. Incorrect data detected"));
    }

    @Test
    @Order(12)
    void postClientWithExistingEmail() throws Exception{
        mvc.perform(post("/rest/clients/addOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "maryyyyyy@psu.ru",
                        "age": 22,
                        "educated": "false",
                        "birth_date": "1998-12-14",
                        "growth": 1.63
                        }""".stripIndent()))
                .andExpect(status().isOk());
    }
    @Test
    @Order(13)
    void updateClientChangeEmailToExistingEmail() throws Exception{
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name": "Mary",
                        "email": "ilyusha55@psu.ru",
                        "age": 22,
                        "educated": "false",
                        "birth_date": "1998-12-14",
                        "growth": 1.63
                        }""".stripIndent()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    void deleteAllClients() throws Exception{
        mvc.perform(delete("/rest/clients/delAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted all clients"));
    }
}