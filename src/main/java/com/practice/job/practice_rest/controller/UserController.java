package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.model.Role;
import com.practice.job.practice_rest.model.User;
import com.practice.job.practice_rest.security.token.GetTokenServiceImpl;
import com.practice.job.practice_rest.service.role.RoleRepository;
import com.practice.job.practice_rest.service.user.UserData;
import com.practice.job.practice_rest.service.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

@Controller
@RestController
@ResponseBody
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping(path = "/rest/users/create")
    public
    String createUser(@RequestBody UserData newUser) {
        logger.info("Creating user start");
        try {
            Role role;
            if (newUser.getRole().isEmpty() & newUser.getRole().equals("read")) {
                role = roleRepository.findById(2).get();
            } else {
                role = roleRepository.findByType(newUser.getRole().toLowerCase(Locale.ROOT));
                if (role==null) {
                    logger.info("Can't create user. Error: given role is not supported");
                    return "Can't create user. Error: given role is not supported";
                }
            }
            User user = new User(newUser.getLogin(), newUser.getPassword(), role);
            try {
                userRepository.save(user);
            } catch (DataIntegrityViolationException e) {
                logger.info("Not create the user. Error: " + e.getMostSpecificCause().getMessage());
                return "Not create the user\nError: " + e.getMostSpecificCause().getMessage();
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.info("User dont created, error: " + e.getMessage());
            return "User dont created, error: " + e.getMessage();
        }
        logger.info("User created");
        return "User created";
    }

    @PostMapping(path = "/getToken")
    public
    String getToken(@RequestBody UserData newUser) {
        logger.info("Getting token for user: "+newUser.getLogin()+" start");
        User user = userRepository.findByLogin(newUser.getLogin());
        if (user==null) {
            logger.info("Not fount user with login="+newUser.getLogin());
            return "Not fount user with login="+newUser.getLogin();
        }
        GetTokenServiceImpl getterToken = new GetTokenServiceImpl();
        String t = getterToken.getToken(newUser.getLogin(), newUser.getPassword(), user);
        if (t.toLowerCase(Locale.ROOT).contains("error")){
            logger.info("Token not create, error: "+t);
            return t;
        }
        logger.info("Token for user: "+newUser.getLogin()+" responded");
        return t;
    }

}
