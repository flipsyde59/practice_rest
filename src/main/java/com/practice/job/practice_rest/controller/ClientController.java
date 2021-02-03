package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.client.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RestController
@RequestMapping(path = "/rest/clients")
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;
//    @PostMapping(path = "/addOnes")
//    public @ResponseBody
//    String addNewClientt(@RequestBody Client client) {
//        logger.info("Adding one client starts");
//        String result = clientService.addNewClient(client);
//        logger.info(result);
//        return result;
//    }
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(path = "/addMany")
    public @ResponseBody
    String addNewClients(@RequestBody List<Client> clients) {
        logger.info("Adding many clients starts");
        String result = clientService.addNewClients(clients);
        logger.info(result);
        return result;
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(path = "/addOne")
    public @ResponseBody
    String addNewClient(@RequestBody Client client) {
        logger.info("Adding one client starts");
        String result = clientService.addNewClient(client);
        logger.info(result);
        return result;
    }

    @GetMapping(path = "/")
    public @ResponseBody
    Object getAllClients() {
        logger.info("Get all clients");
        return clientService.getAllClients();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    ResponseEntity<?> getClientById(@PathVariable(name = "id") Integer id) {
        Client result = clientService.getClientById(id);
        if (result == null) {
            logger.info("Client with that id=" + id + " not found");
            return new ResponseEntity<>("Client with that id=" + id + " not found", HttpStatus.OK);
        }
        logger.info("Get one client id=" + id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping(value = "/{id}")
    public @ResponseBody
    String updateClientById(@PathVariable(name = "id") Integer id, @RequestBody Client client) {
        logger.info("Starting update client with id=" + id);
        String result = clientService.updateClientById(id, client);
        logger.info(result);
        return result;
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(value = "/{id}")
    public String deleteClientById(@PathVariable(name = "id") Integer id) {
        logger.info("Start deleting client with id=" + id);
        String result = clientService.deleteClientById(id);
        logger.info(result);
        return result;
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping(value = "/delAll")
    public String deleteAllClients() {
        logger.info("Start deleting all clients");
        String result = clientService.deleteAllClients();
        logger.info(result);
        return result;
    }
}
