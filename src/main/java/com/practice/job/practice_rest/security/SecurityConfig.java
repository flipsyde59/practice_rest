package com.practice.job.practice_rest.security;

import com.practice.job.practice_rest.model.User;
import com.practice.job.practice_rest.security.filter.TokenAuthenticationEntryPoint;
import com.practice.job.practice_rest.security.filter.TokenAuthenticationFilter;
import com.practice.job.practice_rest.security.filter.TokenAuthenticationManager;
import com.practice.job.practice_rest.service.user.UserData;
import com.practice.job.practice_rest.service.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userDetailsService;

    @Autowired
    TokenAuthenticationManager tokenAuthenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(restTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/getToken").permitAll()
                .anyRequest().authenticated();
    }

    @Bean(name = "restTokenAuthenticationFilter")
    public TokenAuthenticationFilter restTokenAuthenticationFilter() {
        tokenAuthenticationManager.setUserDetailsService(userDetailsService);
        TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint = new TokenAuthenticationEntryPoint();
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenAuthenticationManager, tokenAuthenticationEntryPoint);
        return tokenAuthenticationFilter;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(new ShaPasswordEncoder());
//        auth.authenticationProvider(tokenAuthenticationProvider);
//    }
}