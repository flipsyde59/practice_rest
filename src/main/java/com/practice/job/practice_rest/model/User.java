package com.practice.job.practice_rest.model;

import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"login"}, name="user_login")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String login;
    private byte[] salt = new byte[16];
    private byte[] hash;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST) //организация связи многие ко многим EAGER это что б можно было получить значение
    @JoinTable(name="user_role",joinColumns =                           //связ идет через таблицу user_role
    @JoinColumn(name = "user_id"),inverseJoinColumns =                  //колонка которая связвыает таблицу user с user_role
    @JoinColumn(name = "role_id"))                                      //связь через эту колонку идет к таблице role
    private List<Role> role;

    public User(String login, String password, Role role) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.login = login;
        SecureRandom random = new SecureRandom();
        random.nextBytes(this.salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        this.hash = f.generateSecret(spec).getEncoded();
        this.role = new ArrayList<Role>();
        this.role.add(role);
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public Collection<Role> getRoles(){
        return this.role;
    }
}
