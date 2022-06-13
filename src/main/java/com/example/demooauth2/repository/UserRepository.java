package com.example.demooauth2.repository;

import com.example.demooauth2.entity.authorization.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u from User u where u.username = :name")
    User findUserByUsername(@Param("name") String username);
}
