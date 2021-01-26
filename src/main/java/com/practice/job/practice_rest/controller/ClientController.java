package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.client.ClientRepository;
import com.practice.job.practice_rest.service.client.ClientString;
import com.practice.job.practice_rest.service.client.ParserClient;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RestController
@RequestMapping(path = "/rest/clients")
public class ClientController {

    Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping(path = "/addMany")
    public @ResponseBody
    String addNewClients(@RequestBody List<ClientString> clients) {
        List<String> errors = new ArrayList<>();
        logger.info("Adding many clients starts");
        for (ClientString clientString : clients) {
            ParserClient parserClient = new ParserClient();
            parserClient.FromString(clientString);
            String status = parserClient.getStatus();
            String email = clientString.getEmail();
            if (!status.equals("Ok")) {
                errors.add("For client with email: " + email + "\n" + status);
                logger.info("Not added client with email=" + email + "\n" + status);
            } else {
                try {
                    clientRepository.save(parserClient.getClient());
                    logger.info("Client with email=" + email + " was added");
                } catch (DataIntegrityViolationException e) {
                    logger.info("Not added client with email=" + email);
                    errors.add("For client with email: " + email + " not saved\nError: " + e.getMostSpecificCause().getMessage());
                }
            }
        }
        if (errors.isEmpty()) {
            logger.info("All clients were added");
            return "Saved";
        } else {
            logger.info("Some errors were get:\n" + StringUtils.join(errors, '\n'));
            return "Excepted errors:\n" + StringUtils.join(errors, '\n');
        }
    }

    @PostMapping(path = "/addOne")
    public @ResponseBody
    String addNewClient(@RequestBody ClientString clientString) {
        logger.info("Adding one client starts");
        ParserClient parserClient = new ParserClient();
        parserClient.FromString(clientString);
        String status = parserClient.getStatus();
        if (!status.equals("Ok")) {
            logger.info("Client was not added.\n" + status);
            return status;
        } else {
            try {
                clientRepository.save(parserClient.getClient());
                logger.info("Client was added");
            } catch (DataIntegrityViolationException e) {
                logger.info("Not added client. Error: " + e.getMostSpecificCause().getMessage());
                return "Not saved\nError: " + e.getMostSpecificCause().getMessage();
            }
            return "Saved";
        }
    }

    @GetMapping(path = "/")
    public @ResponseBody
    Object getAllUsers() {
        logger.info("Get all clients");
        Iterable<Client> clients = clientRepository.findAll();
        if (((ArrayList) clients).isEmpty()){
            return "No clients in database";
        }
        return clientRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody
    ResponseEntity<?> getClientById(@PathVariable(name = "id") Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            logger.info("Get one client with not existing id=" + id);
            return new ResponseEntity<>("Client with that id " + id + " not found", HttpStatus.OK);
        }
        Client client = optionalClient.orElseGet(Client::new);
        logger.info("Get one client id=" + id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody
    String updateClientById(@PathVariable(name = "id") Integer id, @RequestBody ClientString clientString) {
        logger.info("Starting update client with id=" + id);
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            logger.info("Client with that id=" + id + " not found");
            return "Client with that id=" + id + " not found";
        }
        ParserClient parserClient = new ParserClient();
        parserClient.FromString(clientString);
        String status = parserClient.getStatus();
        if (!status.equals("Ok")) {
            logger.info("Client was not updated.\n" + status);
            return status;
        }
        Client updClient = parserClient.getClient();
        Client client = optionalClient.orElseGet(Client::new);
        List<String> fields = new ArrayList<>();
        if (!client.getName().equals(updClient.getName())) {
            clientRepository.updateName(id, updClient.getName());
            fields.add("name");
        }
        if (!client.getEmail().equals(updClient.getEmail())) {
            try {
                clientRepository.updateEmail(id, updClient.getEmail());
            } catch (DataIntegrityViolationException e) {
                logger.info("Not update the client. Error: " + e.getMostSpecificCause().getMessage());
                return "Not updated\nError: " + e.getMostSpecificCause().getMessage();
            }
            fields.add("email");
        }
        if (!client.getAge().equals(updClient.getAge())) {
            clientRepository.updateAge(id, updClient.getAge());
            fields.add("age");
        }
        if (!(client.getEducated() == updClient.getEducated())) {
            clientRepository.updateEducated(id, updClient.getEducated());
            fields.add("educated");
        }
        if (client.getBirth_date().compareTo(updClient.getBirth_date()) != 0) {
            clientRepository.updateBirth_date(id, updClient.getBirth_date());
            fields.add("birth_date");
        }
        if (!client.getGrowth().equals(updClient.getGrowth())) {
            clientRepository.updateGrowth(id, updClient.getGrowth());
            fields.add("growth");
        }
        if (fields.isEmpty()) {
            logger.info("Nothing have been changed");
            return "Nothing have been changed";
        } else {
            logger.info("Fields that have been updated: " + StringUtils.join(fields, '|'));
            return "Fields that have been updated:\n" + StringUtils.join(fields, '|');
        }
    }

    @DeleteMapping(value = "/{id}")
    public String deleteClientById(@PathVariable(name = "id") Integer id) {
        logger.info("Start deleting client with id=" + id);
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            logger.info("Nothing have been changed");
            return "Nothing have been changed";
        } else {
            clientRepository.deleteById(id);
            logger.info("The client have been deleted");
            return "The client have been deleted";
        }
    }

    @DeleteMapping(value = "/delAll")
    public String deleteAllClients() {
        logger.info("Start deleting all clients");
        clientRepository.deleteAll();
        logger.info("Completed deleting all clients");
        return "Successfully deleted all clients";
    }
}
