package com.backend.ride.sharing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long driverId;

    @Column(nullable = false)
    private String driverName;

    @Column(nullable = false)
    private Long travelerId;

    // Source coordinates
    private double sourceLatitude;
    private double sourceLongitude;

    // Destination coordinates
    private double destinationLatitude;

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    private double destinationLongitude;

    @Column(nullable = false)
    private Double fare;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String feedback;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public Long getTravelerId() { return travelerId; }
    public void setTravelerId(Long travelerId) { this.travelerId = travelerId; }

    public Double getFare() { return fare; }
    public void setFare(Double fare) { this.fare = fare; }

    public LocalDateTime getStartTime() { return startTime; }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
