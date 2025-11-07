package com.example.devicemanagementmicroservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "device")
public class Device implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "maximumConsumption", nullable = false)
    private double maximumConsumption;

    @Column(name = "yearOfManufacture", nullable = false)
    private int yearOfManufacture;

    @ManyToOne
    @JoinColumn(name = "user_replica_id")
    private UserReplica userReplica;

    public Device() {
    }

    public Device(String name, double maximumConsumption, int yearOfManufacture, UserReplica userReplica) {
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.yearOfManufacture = yearOfManufacture;
        this.userReplica = userReplica;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaximumConsumption() {
        return maximumConsumption;
    }

    public void setMaximumConsumption(double maximumConsumption) {
        this.maximumConsumption = maximumConsumption;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public UserReplica getUserReplica() {
        return userReplica;
    }

    public void setUserReplica(UserReplica userReplica) {
        this.userReplica = userReplica;
    }
}



