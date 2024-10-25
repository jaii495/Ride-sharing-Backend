package com.backend.ride.sharing.repository;

import com.backend.ride.sharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be defined here if needed
    User findByEmail(String email);
}

