package com.physiotrack.usermanagement.service.impl;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;
import com.physiotrack.usermanagement.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerPhysiotherapist(User user) {
        user.setRole("PHYSIO");
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers(String role) {
        if (role != null && !role.isEmpty()) {
            return userRepository.findByRole(role.toUpperCase());
        }
        return userRepository.findAll();
    }

    @Override
    public User deactivateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
