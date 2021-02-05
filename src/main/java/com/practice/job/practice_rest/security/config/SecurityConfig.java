package com.practice.job.practice_rest.security.config;

import com.practice.job.practice_rest.security.filter.TokenAuthenticationEntryPoint;
import com.practice.job.practice_rest.security.filter.TokenAuthenticationFilter;
import com.practice.job.practice_rest.security.filter.TokenAuthenticationManager;
import com.practice.job.practice_rest.service.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
//                .antMatchers(HttpMethod.POST,"/rest/**").hasAuthority("admin")
//                .antMatchers(HttpMethod.PUT,"/rest/**").hasAuthority("admin")
//                .antMatchers(HttpMethod.DELETE,"/rest/**").hasAuthority("admin")
//                .antMatchers(HttpMethod.GET,"/rest/**").hasAnyAuthority("read", "admin")
                .antMatchers("/", "/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                //Перенарпавление на главную страницу после успешного входа
                .defaultSuccessUrl("/")
                .permitAll();
    }

    @Bean(name = "restTokenAuthenticationFilter")
    public TokenAuthenticationFilter restTokenAuthenticationFilter() {
        tokenAuthenticationManager.setUserDetailsService(userDetailsService);
        TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint = new TokenAuthenticationEntryPoint();
        return new TokenAuthenticationFilter(tokenAuthenticationManager, tokenAuthenticationEntryPoint);
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .jdbcAuthentication()
//                .usersByUsernameQuery("select login, hash from user where login=?")
//                .authoritiesByUsernameQuery("select type from role join user_role on user_role.role_id=role.id join user on user.id=user_role.user_id and user.login=?");
//
//    }

//    @Bean(name = "dataSource")
//    public DriverManagerDataSource dataSource() {
//        String driverClassName, url, usernamedb, password;
//        try {
//            FileInputStream fis;
//            Properties property = new Properties();
//            fis = new FileInputStream("src/main/resources/application.properties");
//            property.load(fis);
//            driverClassName = property.getProperty("spring.datasource.driver-class-name");
//            url = property.getProperty("spring.datasource.url");
//            usernamedb = property.getProperty("spring.datasource.username");
//            password = property.getProperty("spring.datasource.password");
//        } catch (IOException e) {
//            throw new AuthenticationServiceException("Error connect to db");
//        }
//        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//        driverManagerDataSource.setDriverClassName(driverClassName);
//        driverManagerDataSource.setUrl(url);
//        driverManagerDataSource.setUsername(usernamedb);
//        driverManagerDataSource.setPassword(password);
//        return driverManagerDataSource;
//    }
}