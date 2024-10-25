package com.backend.ride.sharing.repository;

import com.backend.ride.sharing.model.TripRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRequestRepository extends JpaRepository<TripRequest, Long> {
    // Additional query methods can be defined here
}

