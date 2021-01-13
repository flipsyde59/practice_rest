package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.ClientRepository;
import com.practice.job.practice_rest.service.ClientString;
import com.practice.job.practice_rest.service.ParserClient;
import org.apache.tomcat.util.buf.StringUtils;
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
@RequestMapping(path="/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewClients (@RequestBody List<ClientString> clients) {
        List<String> errors = new ArrayList<>();
        for (ClientString clientString :clients){
            ParserClient parserClient = new ParserClient();
            parserClient.FromString(clientString);
            String status = parserClient.getStatus();
            if (!status.equals("Ok")){
                errors.add("For client with email: "+clientString.getEmail() + "\n" + status);
            }
            try {
                clientRepository.save(parserClient.getClient());
                }
            catch (DataIntegrityViolationException e) {
                errors.add("For client with email: "+clientString.getEmail() + " not saved\nError: "+e.getMostSpecificCause().getMessage());}
        }
    return errors.isEmpty()
            ? "Saved"
            : "Exepted errors:\n" + StringUtils.join(errors, '\n');
    }

    //For one client
    //@PostMapping(path="/add")
    //public @ResponseBody String addNewClient (@RequestBody ClientString clientString) {
    //    ParserClient parserClient = new ParserClient();
    //    parserClient.FromString(clientString);
    //    String status = parserClient.getStatus();
    //    if (!status.equals("Ok")){
    //        return status;
    //    }
    //    try {
    //        clientRepository.save(parserClient.getClient());
    //    }
    //    catch (DataIntegrityViolationException e) {
    //        return "Not saved\nError: "+e.getMostSpecificCause().getMessage();}
    //    return "Saved";
    //}

    @GetMapping(path="/")
    public @ResponseBody Iterable<Client> getAllUsers() {
        return clientRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> read(@PathVariable(name = "id") Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) return new ResponseEntity<>("Client with that id "+id+" not found", HttpStatus.OK);
        Client client = optionalClient.orElseGet(Client::new);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody String update(@PathVariable(name = "id") Integer id, @RequestBody ClientString clientString) {
        ParserClient parserClient = new ParserClient();
        parserClient.FromString(clientString);
        String status = parserClient.getStatus();
        if (!status.equals("Ok")){
            return status;
        }
        Client updClient = parserClient.getClient();
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) return "Client with that id "+id+" not found";
        Client client = optionalClient.orElseGet(Client::new);
        List<String> text = new ArrayList<>();
        if(!client.getName().equals(updClient.getName())){
            clientRepository.updateName(id,updClient.getName());
            text.add("name");
        }
        if(!client.getEmail().equals(updClient.getEmail()) ){
            clientRepository.updateEmail(id,updClient.getEmail());
            text.add("email");
        }
        if(!client.getAge().equals(updClient.getAge()) ){
            clientRepository.updateAge(id,updClient.getAge());
            text.add("age");
        }
        if(!(client.getEducated() == updClient.getEducated()) ){
            clientRepository.updateEducated(id,updClient.getEducated());
            text.add("educated");
        }
        if(client.getBirth_date().compareTo(updClient.getBirth_date())!=0){
            clientRepository.updateBirth_date(id,updClient.getBirth_date());
            text.add("birth_date");
        }
        if(!client.getGrowth().equals(updClient.getGrowth())){
            clientRepository.updateGrowth(id,updClient.getGrowth());
            text.add("growth");
        }
        return text.isEmpty()
                ? "Nothing have been changed"
                : "Fields that have been updated:\n" + StringUtils.join(text, '|');
    }

    @DeleteMapping(value = "/clients/{id}")
    public String delete(@PathVariable(name = "id") Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            return "Nothing have been changed";
        } else {
            clientRepository.deleteById(id);
            return "The client have been deleted";
        }
    }
}