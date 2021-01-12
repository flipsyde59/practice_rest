package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.ClientRepository;
import com.practice.job.practice_rest.service.ClientString;
import com.practice.job.practice_rest.service.ParserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(path="/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewClient (@RequestBody ClientString clientString) {

        ParserClient parserClient = new ParserClient();
        parserClient.FromString(clientString);
        String status = parserClient.getStatus();
        if (!status.equals("Ok")){
            return status;
        }
        try {
            clientRepository.save(parserClient.getClient());
            return "Saved";}
        catch (DataIntegrityViolationException e) {
            return "Not saved\nError: "+e.getMostSpecificCause().getMessage();}
    }

    @GetMapping(path="/")
    public @ResponseBody Iterable<Client> getAllUsers() {
        // This returns a JSON or XML with the users
        return clientRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> read(@PathVariable(name = "id") Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        Client client = optionalClient.orElseGet(Client::new);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable(name = "id") Integer id, @RequestBody ClientString clientString) {
        ParserClient parserClient = new ParserClient();
        parserClient.FromString(clientString);
        Client updClient = parserClient.getClient();
        Optional<Client> optionalClient = clientRepository.findById(id);
        Client client = optionalClient.orElseGet(Client::new);
        if (client.isEqual(updClient)){
            //код для апдейта
        }
        //final boolean updated = clientRepository.updateEmail(, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    //@DeleteMapping(value = "/clients/{id}")
    //public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
    //    final boolean deleted = clientService.delete(id);
//
    //    return deleted
    //            ? new ResponseEntity<>(HttpStatus.OK)
    //            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    //}
}