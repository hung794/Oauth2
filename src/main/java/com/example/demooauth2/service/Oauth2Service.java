package com.example.demooauth2.service;

import com.example.demooauth2.entity.authorization.AuthorizationCode;
import com.example.demooauth2.entity.authorization.Client;
import com.example.demooauth2.entity.authorization.Credential;
import com.example.demooauth2.entity.authorization.User;
import com.example.demooauth2.repository.AuthCodeRepository;
import com.example.demooauth2.repository.ClientRepository;
import com.example.demooauth2.repository.CredentialRepository;
import com.example.demooauth2.repository.UserRepository;
import com.example.demooauth2.util.HandleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class Oauth2Service {
    @Autowired
    private AuthCodeRepository authCodeRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean getClient(Client client) {
        Client client1 = clientRepository.findClientByClientId(client.getClientId());
        if (client1 == null) {
            clientRepository.save(client);
        }
        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return false;
        } else return user.getPassword().equals(password);
    }

    public boolean register(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            return false;
        } else {
            User userNew = new User();
            userNew.setUsername(username);
            userNew.setPassword(password);
            userRepository.save(userNew);
            return true;
        }
    }

    public AuthorizationCode generateAuthCode(String clientId, long userId) {
        AuthorizationCode authorizationCode = new AuthorizationCode();
        authorizationCode.setClientId(clientId);
        authorizationCode.setCode(UUID.randomUUID().toString());
        authorizationCode.setExpiredTime(HandleDate.convertDateToLong(1));
        authorizationCode.setUserId(userId);
        return authorizationCode;
    }

    public Credential generateCredential(long userId, String clientId, List<String> scopes) {
        Credential credential = new Credential();
        credential.setAccessToken(UUID.randomUUID().toString());
        credential.setScope(scopes);
        credential.setClientId(clientId);
        credential.setUserId(userId);
        credential.setExpiredTime(HandleDate.convertDateToLong(7));
        return credential;
    }
}
