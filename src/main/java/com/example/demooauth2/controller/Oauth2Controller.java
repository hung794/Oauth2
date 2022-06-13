package com.example.demooauth2.controller;

import com.example.demooauth2.entity.authorization.AuthorizationCode;
import com.example.demooauth2.entity.authorization.Client;
import com.example.demooauth2.entity.authorization.Credential;
import com.example.demooauth2.entity.authorization.User;
import com.example.demooauth2.entity.dto.AuthorizationInformation;
import com.example.demooauth2.entity.dto.LoginDto;
import com.example.demooauth2.repository.response.Response;
import com.example.demooauth2.repository.AuthCodeRepository;
import com.example.demooauth2.repository.ClientRepository;
import com.example.demooauth2.repository.CredentialRepository;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.service.Oauth2Service;
import com.example.demooauth2.util.CheckUtil;
import com.example.demooauth2.util.HandleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class Oauth2Controller {
    @Autowired
    private Oauth2Service oauth2Service;
    @Autowired
    private AuthCodeRepository authCodeRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckUtil checkUtil;

    private static final HashMap<Long, User> userLogged = new HashMap<>();

    @PostMapping("/client")
    public boolean getClient(@RequestBody Client client) {
        oauth2Service.getClient(client);
        return true;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        if (!oauth2Service.login(loginDto.getUsername(), loginDto.getPassword())) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("User name or password is invalid!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        } else {
            User userLogin = userRepository.findUserByUsername(loginDto.getUsername());
            userLogged.put(userLogin.getId(), userLogin);
            HashMap<String, Long> userId = new HashMap<>();
            userId.put("userId", userLogin.getId());
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Login success!")
                    .withHttpCode(200)
                    .withData(userId)
                    .build(), HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody LoginDto logindto) {
        if (!oauth2Service.register(logindto.getUsername(), logindto.getPassword())) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("User name is already exist!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Register success!")
                    .withHttpCode(200)
                    .build(), HttpStatus.CREATED);
        }
    }

    @PostMapping("/authCode")
    public ResponseEntity createAuthCode(@RequestBody Client client, @RequestParam long userId) {
        if (client == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Client not found!")
                    .build(), HttpStatus.BAD_REQUEST);
        } else if (!checkUtil.checkClient(client)) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Client is invalid!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        } else if (!userLogged.containsKey(userId)) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("You are not logged in!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        } else {
            AuthorizationCode existAuthCode = authCodeRepository.findAuthorizationCodeByClientId(client.getClientId());
            if (existAuthCode != null) {
                authCodeRepository.delete(existAuthCode);
            }
            AuthorizationCode authorizationCode = oauth2Service.generateAuthCode(client.getClientId(), userId);
            authCodeRepository.save(authorizationCode);
            HashMap<String, AuthorizationCode> codeHashMap = new HashMap<>();
            codeHashMap.put("authorizationCode", authorizationCode);
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok!")
                    .withHttpCode(200)
                    .withConsentForm(codeHashMap)
                    .build(), HttpStatus.CREATED);
        }
    }

    @PostMapping("/token")
    public ResponseEntity getToken(@RequestBody AuthorizationInformation authorizationInformation, @RequestParam long userId) {
        AuthorizationCode authorizationCode = authCodeRepository.findAuthorizationCodeByCode(authorizationInformation.getAuthorizationCode());
        Client client = clientRepository.findClientByClientIdAndClientSecret(authorizationInformation.getClientId(), authorizationInformation.getClientSecret());
        if (client == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Client not found!")
                    .withHttpCode(400)
                    .build(), HttpStatus.NOT_FOUND);
        } else if (!checkUtil.checkClient(client)) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Client is invalid!")
                    .withHttpCode(400)
                    .build(), HttpStatus.NOT_FOUND);
        }
        if (!userLogged.containsKey(userId)) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("You are not logged in!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        }
        if (authorizationCode == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Authorization Code not found!")
                    .withHttpCode(400)
                    .build(), HttpStatus.NOT_FOUND);
        } else if (!authorizationCode.getClientId().equals(authorizationInformation.getClientId())) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Client not mapping with Authorization Code!")
                    .withHttpCode(400)
                    .build(), HttpStatus.BAD_REQUEST);
        } else {
            Credential credentialExist = credentialRepository.findCredentialByClientId(authorizationCode.getClientId());
            List<Credential> oldCredentials = credentialRepository.findCredentialByUserId(userId);
            credentialRepository.deleteAll(oldCredentials);
            if (credentialExist != null) {
                credentialRepository.delete(credentialExist);
            }
            Credential credential = oauth2Service.generateCredential(userId, authorizationCode.getClientId(), authorizationInformation.getScope());
            HashMap<String, Credential> credentialHashMap = new HashMap<>();
            credentialHashMap.put("Credential", credential);
            credentialRepository.save(credential);
            authCodeRepository.delete(authorizationCode);
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok!")
                    .withHttpCode(200).withData(credentialHashMap)
                    .build(), HttpStatus.CREATED);
        }
    }

    @PostMapping("/checkToken")
    public ResponseEntity checkToken(@RequestParam String token, @RequestBody Client client) {
        if (!checkUtil.checkClient(client)) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Ok!")
                    .withHttpCode(404)
                    .build(), HttpStatus.NOT_FOUND);
        } else if (credentialRepository.findCredentialByAccessToken(token) == null) {
            return new ResponseEntity<>(Response.ResponseApiBuilder
                    .aResponseApi()
                    .withMessage("Token not found!")
                    .withHttpCode(404)
                    .build(), HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity<>(Response.ResponseApiBuilder
                .aResponseApi()
                .withMessage("Ok!")
                .withHttpCode(200)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/sendToken")
    public Credential sendToken(@RequestParam String token) {
        return credentialRepository.findCredentialByAccessToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestParam long userId) {
        AuthorizationCode authorizationCode = authCodeRepository.findAuthorizationCodeByUserId(userId);
        List<Credential> credential = credentialRepository.findCredentialByUserId(userId);
        if (authorizationCode != null) {
            authCodeRepository.delete(authorizationCode);
        }
        if (credential != null) {
            credentialRepository.deleteAll(credential);
        }
        return new ResponseEntity<>(Response.ResponseApiBuilder
                .aResponseApi()
                .withMessage("Logout success!")
                .withHttpCode(200)
                .build(), HttpStatus.OK);
    }
}