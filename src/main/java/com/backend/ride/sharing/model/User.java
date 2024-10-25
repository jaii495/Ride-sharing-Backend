package com.backend.ride.sharing.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Integer cabNumber;

    private double currentLatitude;
    private double currentLongitude;

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    @ElementCollection
    private List<String> notifications;

    // Enum for Role
    public enum Role {
        ADMIN, TRAVELER, DRIVER
    }

    public List<Long> getTripIds() {
        return tripIds;
    }

    public void setTripIds(List<Long> tripIds) {
        this.tripIds = tripIds;
    }

    @ElementCollection
    private List<Long> tripIds;

    public List<Long> getRequestedTripIds() {
        return requestedTripIds;
    }

    public void setRequestedTripIds(List<Long> requestedTripIds) {
        this.requestedTripIds = requestedTripIds;
    }

    @ElementCollection
    private List<Long> requestedTripIds;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Integer getCabNumber() { return cabNumber; }
    public void setCabNumber(Integer cabNumber) { this.cabNumber = cabNumber; }

    public List<String> getNotifications() { return notifications; }
    public void setNotifications(List<String> notifications) { this.notifications = notifications; }
}
