package com.practice.job.practice_rest.security.filter;

import com.practice.job.practice_rest.model.Role;
import com.practice.job.practice_rest.model.User;
import com.practice.job.practice_rest.security.token.GetTokenServiceImpl;
import com.practice.job.practice_rest.service.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TokenAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserRepository userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            if (authentication instanceof TokenAuthentication) {
                return processAuthentication((TokenAuthentication) authentication);
            } else {
                authentication.setAuthenticated(false);
                return authentication;
            }
        } catch (Exception ex) {
            if (ex instanceof AuthenticationServiceException)
                throw ex;
            return null;
        }
    }

    private TokenAuthentication processAuthentication(TokenAuthentication authentication) throws AuthenticationException {
        String token = authentication.getToken();
        String key = "the_secret_key";
        DefaultClaims claims;
        try {
            claims = (DefaultClaims) Jwts.parser().setSigningKey(key).parse(token).getBody();
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Token corrupted");
        }
        if (claims.get("token_expiration_date", Long.class) == null)
            throw new AuthenticationServiceException("Invalid token");
        Date expiredDate = new Date(claims.get("token_expiration_date", Long.class));
        if (expiredDate.after(new Date()))
            return buildFullTokenAuthentication(authentication, claims);
        else
            throw new AuthenticationServiceException("Token expired date error");
    }

    private TokenAuthentication buildFullTokenAuthentication(TokenAuthentication authentication, DefaultClaims claims) {
        User user = userDetailsService.findByLogin(claims.get("username", String.class));
        Collection<Role> authorities = user.getRoles();
        return new TokenAuthentication(authentication.getToken(), authorities, true, user);
    }

    public void setUserDetailsService(UserRepository repository) {
        this.userDetailsService = repository;
    }

    public String createToken(String username, String password){
        User user = userDetailsService.findByLogin(username);
        if (user == null) {
            return "Error: Not found user with login=" + username;
        }
        GetTokenServiceImpl getterToken = new GetTokenServiceImpl();
        return getterToken.getToken(username, password, user);
    }
}