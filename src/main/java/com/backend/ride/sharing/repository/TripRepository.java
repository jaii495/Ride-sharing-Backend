package com.backend.ride.sharing.repository;

import com.backend.ride.sharing.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    // No additional methods required at the moment, as JpaRepository provides all necessary CRUD methods
}
