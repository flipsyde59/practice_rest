package com.practice.job.practice_rest.service.user;

import com.practice.job.practice_rest.model.Role;
import com.practice.job.practice_rest.model.User;
import com.practice.job.practice_rest.security.token.GetTokenServiceImpl;
import com.practice.job.practice_rest.service.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public String createUser (UserData newUser){
        try {
            Role role;
            if (newUser.getRole().isEmpty() | newUser.getRole().equals("read")) {
                role = roleRepository.findById(2).get();
            } else {
                role = roleRepository.findByType(newUser.getRole().toLowerCase(Locale.ROOT));
                if (role==null) {
                    return "Can't create user. Error: given role is not supported";
                }
            }
            User user = User.builder().login(newUser.getLogin()).password(newUser.getPassword()).role(role).build();
            try {
                userRepository.save(user);
            } catch (Exception e) {
                return "Not create the user\nError: login already exist";
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            return "User dont created, error: " + e.getMessage();
        }
        return "User created";
    }
    public String getToken(UserData newUser){
        User user = userRepository.findByLogin(newUser.getLogin());
        if (user==null) {
            return "Not fount user with login="+newUser.getLogin();
        }
        GetTokenServiceImpl getterToken = new GetTokenServiceImpl();
        String t = getterToken.getToken(newUser.getLogin(), newUser.getPassword(), user);
        if (t.isEmpty()){
            return "Token not create, auth error";
        }
        return t;
    }
}
