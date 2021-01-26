package com.practice.job.practice_rest.service.client;

import com.practice.job.practice_rest.model.Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserClient {
    private Client client = new Client();
    private String status;



    public void FromString(ClientString client){
        this.status = "";
        this.client.setName(client.getName());
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(client.getEmail());
        if (matcher.matches())
            this.client.setEmail(client.getEmail());
        else
            this.status+="Error: Data can not be parsed\nEmail not valid\nExample: \"name@example.com\"\n";
        this.client.setEducated(Boolean.parseBoolean(client.getEducated()));
        try {
            this.client.setAge(Integer.parseInt(client.getAge()));}
        catch (NumberFormatException e) {
            this.status += "Error: Data can not be parsed\nAge must be an integer\nExample: 25\n";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try{
            this.client.setBirth_date(formatter.parse(client.getBirth_date()));}
        catch (ParseException e) {
            this.status += "Error: Data can not be parsed\nDate must be the \"dd.MM.yyyy\" format\nExample: \"09.09.1999\"\n";
        }
        try {
            this.client.setGrowth(Float.parseFloat(client.getGrowth().replace(',','.')));}
        catch (NumberFormatException e){
            this.status += "Error: Data can not be parsed\nGrowth must be in meters and have a fractional value.\nExample: \"1.59\"\n";

        }
        if (this.client.getBirth_date()!=null & this.client.getEmail()!=null & this.client.getGrowth()!=null & this.client.getEducated()!=null)
            this.status = "Ok";
        else
            this.status += "Unforeseeable error. Incorrect data detected";
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
