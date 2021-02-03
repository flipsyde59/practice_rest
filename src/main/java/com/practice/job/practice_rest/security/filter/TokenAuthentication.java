package com.practice.job.practice_rest.security.filter;

import com.practice.job.practice_rest.model.Role;
import com.practice.job.practice_rest.model.User;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class TokenAuthentication implements Authentication {
    private String token;
    private Collection<Role> authorities;
    private boolean isAuthenticated;
    private User principal;

    public TokenAuthentication() {
    }

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public TokenAuthentication(String token, Collection<Role> authorities, boolean isAuthenticated,
                               User principal) {
        this.token = token;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
        this.principal = principal;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        if (principal != null)
            return principal.getLogin();
        else
            return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        isAuthenticated = b;
    }

    public String getToken() {
        return this.token;
    }

}