package com.example.resourcesserver.util;

import com.example.resourcesserver.entity.Credential;
import org.springframework.web.client.RestTemplate;

public class CheckToken {
    public static Credential checkToken(String token) {
        final String uri = "https://auth-server-v1.herokuapp.com/api/v1/auth/sendToken?token=" + token;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Credential.class);
    }
}