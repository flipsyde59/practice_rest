package com.practice.job.practice_rest.service.client;

import com.practice.job.practice_rest.model.Client;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public String addNewClients(List<Client> clients){
        List<String> errors = new ArrayList<>();
        List<String> success = new ArrayList<>();
        for (Client client : clients) {
            String email = client.getEmail();
            try {
                clientRepository.save(client);
                success.add("Client with email=" + email + " was added");
            } catch (Exception e) {
                success.add("Not added client with email=" + email);
                errors.add("Not added client with email=" + email);
            }
        }
        if (errors.isEmpty()) {
            return StringUtils.join(success, '\n') + "\nAll clients were added";
        }
        return StringUtils.join(success, '\n') + "Excepted errors:\n" + StringUtils.join(errors, '\n');
    }
    public String addNewClient(Client client){
        try {
            clientRepository.save(client);
        } catch (Exception e) {
            return "Not saved Error: email already exist";
        }
        return "Client was added";
    }
    public Object getAllClients(){
        Iterable<Client> clients = clientRepository.findAll();
        if (((ArrayList) clients).isEmpty()){
            return "No clients in database";
        }
        return clients;
    }
    public Client getClientById(Integer id){
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            return null;
        }
        return optionalClient.orElseGet(Client::new);
    }
    public String updateClientById(Integer id, Client updClient){
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            return "Client with that id=" + id + " not found";
        }
        Client client = optionalClient.orElseGet(Client::new);
        List<String> fields = new ArrayList<>();
        if (!client.getName().equals(updClient.getName())) {
            fields.add("name");
        }
        if (!client.getEmail().equals(updClient.getEmail())) {
            fields.add("email");
        }
        if (!client.getAge().equals(updClient.getAge())) {
            fields.add("age");
        }
        if (!(client.getEducated() == updClient.getEducated())) {
            fields.add("educated");
        }
        if (client.getBirth_date().compareTo(updClient.getBirth_date()) != 0) {
            fields.add("birth_date");
        }
        if (!client.getGrowth().equals(updClient.getGrowth())) {
            fields.add("growth");
        }
        if (fields.isEmpty()) {
            return "Nothing have been changed";
        } else {
            try {
                clientRepository.update(id, updClient.getName(),
                        updClient.getEmail(),
                        updClient.getAge(),
                        updClient.getEducated(),
                        updClient.getBirth_date(),
                        updClient.getGrowth());
            } catch (Exception e) {
                return "Not update the client. Error: email already exist";
            }
            return "Fields that have been updated:\n" + StringUtils.join(fields, '|');
        }
    }
    public String deleteClientById(Integer id){
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isEmpty()) {
            return "Nothing have been changed";
        } else {
            clientRepository.deleteById(id);
            return "The client have been deleted";
        }
    }
    public String deleteAllClients() {
        clientRepository.deleteAll();
        return "Successfully deleted all clients";
    }
}
