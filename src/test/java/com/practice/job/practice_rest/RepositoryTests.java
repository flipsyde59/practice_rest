package com.practice.job.practice_rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.job.practice_rest.controller.ClientController;
import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.client.ClientRepository;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties", properties = "security.enabled:false")
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
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
        List<Client> clients = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1995-12-05");
        Client client=new Client("Micael","micael123@psu.ru",25, false, date, (float) 1.8);
        clients.add(client);
        date=sdf.parse("2002-05-19");
        client=new Client("Kate","katekitty@psu.ru",18, false, date, (float) 1.55);
        clients.add(client);
        date=sdf.parse("1998-06-20");
        client=new Client("Oleg","oleg1111@psu.ru",22, true, date, (float) 1.95);
        clients.add(client);
        mvc.perform(post("/rest/clients/addMany")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clients)))
                .andExpect(status().isOk())
                .andExpect(content().string("Client with email=micael123@psu.ru was added\n" +
                                            "Client with email=katekitty@psu.ru was added\n" +
                                            "Client with email=oleg1111@psu.ru was added\n" +
                                            "All clients were added"));
    }

    @Test
    @Order(3)
    void postOneTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1988-12-15");
        Client client=new Client("Ilya","ilyusha55@psu.ru",32, true, date, (float) 1.73);
        mvc.perform(post("/rest/clients/addOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1998-12-14");
        Client client=new Client("Mary","maryyyyyy@psu.ru",22, false, date, (float) 1.63);
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string("Fields that have been updated:\nname|email|age|educated|birth_date|growth"));
    }

    @Test
    @Order(8)
    void updateExistingClientByIdWithoutChange() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1998-12-14");
        Client client=new Client("Mary","maryyyyyy@psu.ru",22, false, date, (float) 1.63);
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string("Nothing have been changed"));
    }

    @Test
    @Order(9)
    void updateNotExistingClientById() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1998-12-14");
        Client client=new Client("Mary","maryyyyyy@psu.ru",22, false, date, (float) 1.63);
        mvc.perform(put("/rest/clients/22")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message","Malformed JSON request").exists());
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message","Malformed JSON request").exists());
    }

    @Test
    @Order(12)
    void postClientWithExistingEmail() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1998-12-14");
        Client client=new Client("Mary","maryyyyyy@psu.ru",22, false, date, (float) 1.63);
        mvc.perform(post("/rest/clients/addOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk());
    }
    @Test
    @Order(13)
    void updateClientChangeEmailToExistingEmail() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=sdf.parse("1998-12-14");
        Client client=new Client("Mary","ilyusha55@psu.ru",22, false, date, (float) 1.63);
        mvc.perform(put("/rest/clients/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
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