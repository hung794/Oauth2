package com.example.demooauth2.util;

import com.example.demooauth2.entity.authorization.Client;
import com.example.demooauth2.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckUtil {
    @Autowired
    private ClientRepository clientRepository;

    public boolean checkClient(Client client) {
        Client clientToCheck = clientRepository.findClientByClientId(client.getClientId());
        if (clientToCheck == null) {
            return false;
        } else return (client.getClientId().equals(clientToCheck.getClientId())
                && client.getRootUrl().equals(clientToCheck.getRootUrl())
                && client.getClientSecret().equals(clientToCheck.getClientSecret()));
    }
}
