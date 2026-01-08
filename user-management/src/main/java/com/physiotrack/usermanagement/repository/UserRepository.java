package com.physiotrack.usermanagement.repository;

import com.physiotrack.usermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find users by their role (e.g., View all Patients)
    List<User> findByRole(String role);
    
    // Find by email for login/registration checks
    User findByEmail(String email);
}