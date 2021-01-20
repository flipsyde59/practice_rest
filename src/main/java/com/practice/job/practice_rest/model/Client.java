package com.practice.job.practice_rest.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name="uk_email")})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer         id;
    private String          name;
    private String          email;
    private Integer         age;
    private Boolean         educated;
    @Temporal(TemporalType.DATE)
    private Date            birth_date;
    private Float           growth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getEducated() {
        return educated;
    }

    public void setEducated(Boolean educated) {
        this.educated = educated;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public Float getGrowth() {
        return growth;
    }

    public void setGrowth(Float growth) {
        this.growth = growth;
    }

}
