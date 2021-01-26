package com.practice.job.practice_rest.security.token;

import com.practice.job.practice_rest.model.User;

public interface GetTokenService {
    String getToken(String username, String password, User user) throws Exception ;
}
