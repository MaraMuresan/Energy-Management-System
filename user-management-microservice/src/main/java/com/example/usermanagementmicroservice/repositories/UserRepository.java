package com.example.usermanagementmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.usermanagementmicroservice.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<User> findByUsername(String username);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT u " +
            "FROM User u " +
            "WHERE u.username = :username " +
            "AND u.age >= 60  ")
    Optional<User> findSeniorsByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
