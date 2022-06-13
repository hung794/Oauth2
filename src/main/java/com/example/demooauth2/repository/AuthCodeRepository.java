package com.example.demooauth2.repository;

import com.example.demooauth2.entity.authorization.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthorizationCode, Long> {
    @Query("SELECT a from AuthorizationCode a where a.clientId = :clientId")
    AuthorizationCode findAuthorizationCodeByClientId(@Param("clientId") String clientId);
    @Query("SELECT a from AuthorizationCode a where a.code = :code")
    AuthorizationCode findAuthorizationCodeByCode(@Param("code") String code);

    AuthorizationCode findAuthorizationCodeByUserId(long userId);
}
