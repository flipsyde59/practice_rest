package com.practice.job.practice_rest.model;

import lombok.Builder;
import lombok.Getter;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"login"}, name="user_login")})
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String login;
    private byte[] salt;
    private byte[] hash;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) //организация связи многие ко многим EAGER это что б можно было получить значение
    @JoinTable(name="user_role",joinColumns =                           //связ идет через таблицу user_role
    @JoinColumn(name = "user_id"),inverseJoinColumns =                  //колонка которая связвыает таблицу user с user_role
    @JoinColumn(name = "role_id"))                                      //связь через эту колонку идет к таблице role
    private List<Role> role;

    private User( Integer id,
             String login,
             byte[] salt,
             byte[] hash,List<Role> role) {
        this.id = id;
        this.login = login;
        this.salt = salt;
        this.hash = hash;
        this.role = role;
    }

    public User(){

    }
    public static class UserBuilder {

        public byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return f.generateSecret(spec).getEncoded();
        }
        public UserBuilder password(String password) throws InvalidKeySpecException, NoSuchAlgorithmException{
            SecureRandom random = new SecureRandom();
            random.nextBytes(this.salt);
            this.hash = getHash(password, this.salt);
            return this;
        }
        public UserBuilder role(Role role){
            this.role = new ArrayList<Role>();
            this.role.add(role);
            return this;
        }
    }
}
