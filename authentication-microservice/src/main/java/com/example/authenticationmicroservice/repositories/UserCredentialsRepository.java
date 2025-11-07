package com.example.authenticationmicroservice.repositories;

import com.example.authenticationmicroservice.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    Optional<UserCredentials> findByUsername(String username);
    boolean existsByUsername(String username);
}
