package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Client;
import com.practice.job.practice_rest.service.ClientRepository;
import com.practice.job.practice_rest.service.ClientString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Controller
@RequestMapping(path="/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewClient (@RequestBody ClientString clientString) throws ParseException {

        Client n = new Client();
        n.setName(clientString.getName());
        n.setEmail(clientString.getEmail());
        n.setAge(Integer.parseInt(clientString.getAge()));
        n.setSex(Boolean.parseBoolean(clientString.getSex()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        n.setBirth_date(formatter.parse(clientString.getBirth_date()));
        n.setGrowth(Float.parseFloat(clientString.getGrowth()));

        String text="";

        try {
            clientRepository.save(n);
            text="Saved";}
        catch (DataIntegrityViolationException e) {
            text="Not saved\nError: "+e.getMostSpecificCause().getMessage();}
        return text;
    }

    @GetMapping(path="/")
    public @ResponseBody Iterable<Client> getAllUsers() {
        // This returns a JSON or XML with the users
        return clientRepository.findAll();
    }

    //@GetMapping(value = "/clients/{id}")
    //public ResponseEntity<Client> read(@PathVariable(name = "id") int id) {
    //    final Client client = clientService.read(id);
//
    //    return client != null
    //            ? new ResponseEntity<>(client, HttpStatus.OK)
    //            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}
//
    //@PutMapping(value = "/clients/{id}")
    //public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody Client client) {
    //    final boolean updated = clientService.update(client, id);
//
    //    return updated
    //            ? new ResponseEntity<>(HttpStatus.OK)
    //            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    //}
//
    //@DeleteMapping(value = "/clients/{id}")
    //public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
    //    final boolean deleted = clientService.delete(id);
//
    //    return deleted
    //            ? new ResponseEntity<>(HttpStatus.OK)
    //            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    //}
}