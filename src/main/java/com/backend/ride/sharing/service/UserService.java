package com.backend.ride.sharing.service;

import com.backend.ride.sharing.model.User;
import com.backend.ride.sharing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User validateUserCredentials(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getPassword().equals(password) ? user : null;
        }
        return null;
    }

    public String updateUserCoordinates(Long userId, double latitude, double longitude) {
        // Check if the user exists
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            // Update the coordinates if user exists
            User user = optionalUser.get();
            user.setCurrentLatitude(latitude);
            user.setCurrentLongitude(longitude);
            userRepository.save(user);  // Save the updated user object
            return "Coordinates updated successfully.";
        } else {
            return "User not found.";
        }
    }

    public boolean checkIfUserExists(long userId) {
        return userRepository.existsById(userId);
    }

    public void addRequestedTripIdToUser(Long userId, Long tripId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Handle user not found

        // Add the trip ID to the user's trip IDs list
        if (user.getRequestedTripIds() == null) {
            user.setRequestedTripIds(new ArrayList<>()); // Initialize if null
        }
        user.getRequestedTripIds().add(tripId);
        userRepository.save(user); // Save the user with updated trip IDs
    }

    public void removeRequestedTripIdFromUser(Long userId, Long tripRequestId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        user.getRequestedTripIds().remove(tripRequestId);
        userRepository.save(user);
    }

    public void addTripIdToUser(Long userId, Long tripId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return;
        }
        User user = optionalUser.get();
        user.getTripIds().add(tripId);
        userRepository.save(user);
    }
}
