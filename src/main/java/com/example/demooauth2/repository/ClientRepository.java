package com.example.demooauth2.repository;

import com.example.demooauth2.entity.authorization.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @Query("SELECT c from Client c where c.clientId = :id")
    Client findClientByClientId(@Param("id") String id);
    @Query("SELECT c from Client c where c.clientId = :id and c.clientSecret = :secret")
    Client findClientByClientIdAndClientSecret(@Param("id") String id, @Param("secret") String secret);
}
