package com.practice.job.practice_rest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name="uk_email")})
@Getter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer         id;
    @Setter
    private String          name;
    @Setter
    private String          email;
    @Setter
    private Integer         age;
    @Setter
    private Boolean         educated;
    @Temporal(TemporalType.DATE)
    @Setter
    private Date            birth_date;
    @Setter
    private Float           growth;

}
