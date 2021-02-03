package com.practice.job.practice_rest.controller;

import com.practice.job.practice_rest.service.user.UserData;
import com.practice.job.practice_rest.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RestController
@ResponseBody
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(path = "/rest/users/create")
    public String createUser(@RequestBody UserData newUser) {
        logger.info("Creating user start");
        String result = userService.createUser(newUser);
        logger.info(result);
        return result;
    }

    @PostMapping(path = "/getToken")
    public String getToken(@RequestBody UserData newUser) {
        logger.info("Getting token for user: " + newUser.getLogin() + " start");
        String token = userService.getToken(newUser);
        logger.info("Token for user: " + newUser.getLogin() + " responded");
        return token;
    }

}
