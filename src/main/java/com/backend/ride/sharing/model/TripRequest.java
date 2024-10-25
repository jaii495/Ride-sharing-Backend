package com.backend.ride.sharing.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trip_requests")
public class TripRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ID of the user requesting the trip

    private double sourceLatitude;
    private double sourceLongitude;
    private double destinationLatitude;
    private double destinationLongitude;

    @Column(nullable = false)
    private double estimatedFare;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    // Method to calculate estimated fare based on distance
    public void calculateFare() {
        double distance = calculateDistance(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
        this.estimatedFare = distance * 30; // 30 Rs per km
    }

    // Haversine formula to calculate distance in kilometers between two points on the Earth
    private double calculateDistance(double srcLat, double srcLon, double destLat, double destLon) {
        final int EARTH_RADIUS = 6371; // Radius of the Earth in km

        double dLat = Math.toRadians(destLat - srcLat);
        double dLon = Math.toRadians(destLon - srcLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(srcLat)) * Math.cos(Math.toRadians(destLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distance in km
    }
}

