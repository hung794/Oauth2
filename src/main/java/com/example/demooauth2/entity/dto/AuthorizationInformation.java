package com.example.demooauth2.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationInformation {

    private String authorizationCode;
    private String clientId;
    private String rootUrl;
    private String clientSecret;
    private List<String> scope;
}
