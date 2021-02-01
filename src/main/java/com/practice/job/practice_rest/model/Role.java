package com.practice.job.practice_rest.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"type"}, name="role_type")})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String type;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)  //организация связи многие ко многим LAZY чтоб нельзя было получить значение за предлами класса
    @JoinTable(name="user_role",joinColumns =                           //связ идет через таблицу user_role
    @JoinColumn(name = "role_id"),inverseJoinColumns =                  //колонка которая связвыает таблицу role с user_role
    @JoinColumn(name = "user_id"))                                      //связь через эту колонку идет к таблице user
    private List<User> user;
    public Integer getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return type;
    }
}
