package com.example.resourcesserver.controller;

import com.example.resourcesserver.entity.Article;
import com.example.resourcesserver.entity.Credential;
import com.example.resourcesserver.entity.response.Response;
import com.example.resourcesserver.repository.ArticleRepository;
import com.example.resourcesserver.repository.CredentialRepository;
import com.example.resourcesserver.util.CheckToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/v1/articles")
@CrossOrigin
public class ArticleController {
    @Autowired
    private CredentialRepository credentialRepository;

    @GetMapping
    public ResponseEntity findAll(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer")) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Forbidden!")
                    .withHttpCode(403)
                    .build(), HttpStatus.FORBIDDEN);
        }
        String token = authorization.substring("Bearer ".length());
        Credential credentialCheck = CheckToken.checkToken(token);
        if (credentialCheck == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Forbidden!")
                    .withHttpCode(403)
                    .build(), HttpStatus.FORBIDDEN);
        } else {
            Credential credential = credentialRepository.findCredentialByAccessToken(token);
            if(credential == null){
                credentialRepository.save(credentialCheck);
            }
        }
        Credential credential = credentialRepository.findCredentialByAccessToken(token);
        if (credential == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Forbidden!")
                    .withHttpCode(403)
                    .build(), HttpStatus.FORBIDDEN);
        } else {
            ResponseEntity response = null;
            if (credential.getScope().size() == 0) {
                response = new ResponseEntity<>(Response.ResponseApiBuilder
                        .aResponseApi()
                        .withMessage("You do not have access!")
                        .withHttpCode(403)
                        .build(), HttpStatus.FORBIDDEN);
            } else {
                for (String scope : credential.getScope()) {
                    if (scope.equals("user")) {
                        response = new ResponseEntity<>(Response.ResponseApiBuilder.aResponseApi()
                                .withMessage("Ok!")
                                .withHttpCode(200)
                                .build(), HttpStatus.OK);
                        break;
                    } else {
                        response = new ResponseEntity<>(Response.ResponseApiBuilder
                                .aResponseApi()
                                .withMessage("You do not have access!")
                                .withHttpCode(403)
                                .build(), HttpStatus.FORBIDDEN);
                    }
                }
            }
            return response;
        }
    }
}