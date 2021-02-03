package com.practice.job.practice_rest.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}, name="uk_email")})
@Getter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Setter
    private String name;
    @Setter
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Error: Data can not be parsed\nEmail not valid\nExample: \"name@example.com\"")
    private String email;
    @Setter
    @Digits(integer=3, fraction=0, message = "Не более 3-х знаков")
    private Integer age;
    @Setter
    private Boolean educated;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat
    @Setter
    private Date birth_date;
    @Setter
    @DecimalMax("3.00")
    private Float growth;

}
