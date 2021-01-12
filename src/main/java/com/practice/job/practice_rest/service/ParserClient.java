package com.practice.job.practice_rest.service;

import com.practice.job.practice_rest.model.Client;
import org.springframework.data.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ParserClient {
    private Client client;
    private String status;


    public void FromString(ClientString client){
        this.client.setName(client.getName());
        this.client.setEmail(client.getEmail());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            this.client.setAge(Integer.parseInt(client.getAge()));
            this.client.setSex(Boolean.parseBoolean(client.getSex()));
            this.client.setBirth_date(formatter.parse(client.getBirth_date()));
            this.client.setGrowth(Float.parseFloat(client.getGrowth()));
            this.status = "Ok";
        } catch (ParseException e) {
            this.status = "Error: Data can not be parsed";
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
