package com.example.demooauth2.repository;

import com.example.demooauth2.entity.authorization.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    @Query(value = "SELECT * from Credential where access_token = :token", nativeQuery = true)
    Credential findCredentialByAccessToken(@Param("token") String token1);

    Credential findCredentialByClientId(String clientId);

    List<Credential> findCredentialByUserId(long userId);
}
