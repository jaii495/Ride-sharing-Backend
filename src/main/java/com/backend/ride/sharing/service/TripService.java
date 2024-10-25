package com.backend.ride.sharing.service;

import com.backend.ride.sharing.model.Trip;
import com.backend.ride.sharing.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip); // Save the trip to the database
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id); // Retrieve a trip by its ID
    }

    public Trip completeTrip(Trip trip) {
        trip.setEndTime(LocalDateTime.now());
        tripRepository.save(trip);
        return trip;
    }

    public Trip giveFeedback(Trip trip, String feedback) {
        trip.setFeedback(feedback);
        tripRepository.save(trip);
        return trip;
    }

    public List<Trip> getAll() {
        return tripRepository.findAll();
    }
}