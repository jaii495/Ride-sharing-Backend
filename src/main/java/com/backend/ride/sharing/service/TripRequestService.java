package com.backend.ride.sharing.service;

import com.backend.ride.sharing.model.TripRequest;
import com.backend.ride.sharing.repository.TripRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripRequestService {

    @Autowired
    private TripRequestRepository tripRequestRepository;

    public TripRequest saveTripRequest(TripRequest tripRequest) {
        return tripRequestRepository.save(tripRequest);
    }

    public Optional<TripRequest> getTripRequestById(Long tripRequestId) {
        return tripRequestRepository.findById(tripRequestId);
    }

    public void deleteTripRequestById(Long tripRequestId) {
        tripRequestRepository.deleteById(tripRequestId);
    }
}
