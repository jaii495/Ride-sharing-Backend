package com.backend.ride.sharing.controller;

import com.backend.ride.sharing.model.Trip;
import com.backend.ride.sharing.model.TripRequest;
import com.backend.ride.sharing.model.User;
import com.backend.ride.sharing.service.TripRequestService;
import com.backend.ride.sharing.service.TripService;
import com.backend.ride.sharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TripRequestService tripRequestService;

    @Autowired
    private TripService tripService;

    @PostMapping("signup")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        User user = userService.validateUserCredentials(email, password);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public static class CoordinateRequest {
        public double latitude;
        public double longitude;
    }

    @PostMapping("/{id}/updateCoordinates")
    public ResponseEntity<String> updateCoordinates(
            @PathVariable Long id,
            @RequestBody CoordinateRequest request) {

        String result = userService.updateUserCoordinates(id, request.latitude, request.longitude);

        if (result.equals("Coordinates updated successfully.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping("/createTripRequest")
    public ResponseEntity<TripRequest> createTripRequest(@RequestBody TripRequest tripRequest) {
        Long id = tripRequest.getUserId();
        if (!userService.checkIfUserExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // User not found, return 404
        }
        tripRequest.calculateFare();
        TripRequest savedTripRequest = tripRequestService.saveTripRequest(tripRequest);
        userService.addRequestedTripIdToUser(id, savedTripRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTripRequest);
    }

    public static class acceptTripBody {
        public Long tripRequestId;
        public Long driverId;
        public double latitude, longitude;
    }

    @PostMapping("/acceptTrip")
    public ResponseEntity<Trip> acceptTripRequest(@RequestBody acceptTripBody tripBody) {
        Long driverId = tripBody.driverId;
        Long tripRequestId = tripBody.tripRequestId;
        double driverLatitude = tripBody.latitude;
        double driverLongitude = tripBody.longitude;

        Optional<User> optionalUser = userService.getUserById(driverId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        User user = optionalUser.get();
        if (user.getRole() != User.Role.DRIVER) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Optional<TripRequest> optionalTripRequest = tripRequestService.getTripRequestById(tripRequestId);
        if (optionalTripRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                    .body(null);
        }
        TripRequest tripRequest = optionalTripRequest.get();

        double distance = calculateDistance(
                driverLatitude, driverLongitude,
                tripRequest.getSourceLatitude(), tripRequest.getSourceLongitude());

        // Check if the driver is within 100 meters
        if (distance > 0.1) { // 100 meters = 0.1 kilometers
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(null);
        }

        tripRequestService.deleteTripRequestById(tripRequestId);

        // Remove the trip request ID from the traveler's tripIds list
        userService.removeRequestedTripIdFromUser(tripRequest.getUserId(), tripRequestId);

        Trip newTrip = new Trip();
        newTrip.setDriverId(driverId);
        newTrip.setTravelerId(tripRequest.getUserId());
        newTrip.setDriverName(user.getName());
        newTrip.setSourceLatitude(tripRequest.getSourceLatitude());
        newTrip.setSourceLongitude(tripRequest.getSourceLongitude());
        newTrip.setDestinationLatitude(tripRequest.getDestinationLatitude());
        newTrip.setDestinationLongitude(tripRequest.getDestinationLongitude());
        newTrip.setFare(tripRequest.getEstimatedFare());
        newTrip.setStartTime(LocalDateTime.now());

        Trip savedTrip = tripService.saveTrip(newTrip);

        userService.addTripIdToUser(driverId, savedTrip.getId());
        userService.addTripIdToUser(tripRequest.getUserId(), savedTrip.getId());

        // Return success response
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedTrip);
    }

    @PostMapping("/completeTrip")
    public ResponseEntity<Trip> completeTrip(@RequestBody acceptTripBody tripBody) {
        Long driverId = tripBody.driverId;
        Long tripId = tripBody.tripRequestId;
        double driverLatitude = tripBody.latitude;
        double driverLongitude = tripBody.longitude;

        Optional<User> optionalUser = userService.getUserById(driverId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        User user = optionalUser.get();
        if (user.getRole() != User.Role.DRIVER) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        Optional<Trip> optionalTrip = tripService.getTripById(tripId);
        if (optionalTrip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(null);
        }
        Trip trip = optionalTrip.get();
        if (!Objects.equals(trip.getDriverId(), driverId)) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                    .body(null);
        }
        double distance = calculateDistance(
                driverLatitude, driverLongitude,
                trip.getDestinationLatitude(), trip.getDestinationLongitude());
        if (distance > 0.1) { // 100 meters = 0.1 kilometers
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(tripService.completeTrip(trip));
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }

    public static class FeedbackBody {
        public Long userId, tripId;
        public String feedback;
    }

    @PostMapping("/giveFeedback")
    public ResponseEntity<Trip> giveFeedback(@RequestBody FeedbackBody feedbackBody) {
        Long userId = feedbackBody.userId;
        Long tripId = feedbackBody.tripId;
        String feedback = feedbackBody.feedback;
        Optional<User> optionalUser = userService.getUserById(userId);
        Optional<Trip> optionalTrip = tripService.getTripById(tripId);
        if (optionalUser.isEmpty() || optionalTrip.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        Trip trip = optionalTrip.get();
        User user = optionalUser.get();

        if (!Objects.equals(user.getId(), trip.getTravelerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        if (trip.getFeedback() != null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(tripService.giveFeedback(trip, feedback));
    }

    @PostMapping("/getAudit")
    public ResponseEntity<List<Trip>> getAudit(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(null);
        }
        User user = optionalUser.get();
        List<Trip> audits = new ArrayList<>();
        List<Long> allTripIds = user.getTripIds();
        for (Long tripId : allTripIds) {
            Optional<Trip> optionalTrip = tripService.getTripById(tripId);
            if (optionalTrip.isPresent()) {
                Trip trip = optionalTrip.get();
                if (trip.getEndTime() != null) {
                    audits.add(trip);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(audits);
    }

    @PostMapping("/viewAllFeedbacks")
    public ResponseEntity<List<Trip>> viewAllFeedbacks(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        User user = optionalUser.get();
        if (user.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        List<Trip> feedbackTrips = new ArrayList<>();
        List<Trip> trips = tripService.getAll();
        for (Trip trip : trips) {
            if (trip.getFeedback() != null) {
                feedbackTrips.add(trip);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(feedbackTrips);
    }

    @PostMapping("/viewAllTrips")
    public ResponseEntity<List<Trip>> viewAllTrips(@RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        User user = optionalUser.get();
        if (user.getRole() != User.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        List<Trip> trips = tripService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(trips);
    }
}