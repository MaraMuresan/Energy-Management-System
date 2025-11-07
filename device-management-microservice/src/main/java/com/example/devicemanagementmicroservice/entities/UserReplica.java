package com.example.devicemanagementmicroservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
        import java.util.UUID;

@Entity
@Table(name = "user_replica")
public class UserReplica implements Serializable {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username")
    private String username;

    public UserReplica() {}

    public UserReplica(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}