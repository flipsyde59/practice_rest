package com.practice.job.practice_rest.security.token;

import com.practice.job.practice_rest.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

@Service
public class GetTokenServiceImpl implements GetTokenService {

    @Override
    public String getToken(String username, String password, User user){
        if (username == null || password == null)
            return null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), user.getSalt(), 65536, 128);
        byte[] hash;
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException  e) {
            return "Error" +e.getMessage();
        }
        Map<String, Object> tokenData = new HashMap<>();
        if (Arrays.equals(hash, user.getHash())) {
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getId().toString());
            tokenData.put("username", user.getLogin());
            tokenData.put("token_create_date", new Date().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 100);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            String key = "the_secret_key";
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
        } else {
            return "Authentication error";
        }
    }

}
