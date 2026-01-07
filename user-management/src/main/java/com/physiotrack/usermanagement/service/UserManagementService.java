package com.physiotrack.usermanagement.service;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    // UC25: Register Physiotherapist
    public User registerPhysiotherapist(User user) {
        user.setRole("PHYSIO");
        user.setActive(true);
        // Logic to generate temp password could go here if needed
        return userRepository.save(user);
    }

    // UC26: View Registered Users
    public List<User> getAllUsers(String role) {
        if (role != null && !role.isEmpty()) {
            return userRepository.findByRole(role.toUpperCase());
        }
        return userRepository.findAll();
    }

    // UC27: Deactivate User Account
    public User deactivateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false); // Soft delete
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }
}
