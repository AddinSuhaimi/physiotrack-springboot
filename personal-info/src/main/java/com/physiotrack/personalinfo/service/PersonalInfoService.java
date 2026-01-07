package com.physiotrack.personalinfo.service;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalInfoService {

    @Autowired
    private UserRepository userRepository;

    // UC04: Edit Profile
    public User updateProfile(Long userId, User updatedData) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            
            // Only update allowed fields (don't overwrite ID, role, or password here)
            if (updatedData.getUsername() != null) existingUser.setUsername(updatedData.getUsername());
            if (updatedData.getPhone() != null) existingUser.setPhone(updatedData.getPhone());
            if (updatedData.getAddress() != null) existingUser.setAddress(updatedData.getAddress());
            if (updatedData.getGender() != null) existingUser.setGender(updatedData.getGender());
            if (updatedData.getProfileImageUrl() != null) existingUser.setProfileImageUrl(updatedData.getProfileImageUrl());
            
            return userRepository.save(existingUser);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    // UC05: Choose Preferred Language
    public User updateLanguage(Long userId, String languageCode) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Validate language code (simple check)
            if (languageCode.equals("en") || languageCode.equals("ms") || languageCode.equals("zh")) {
                user.setLanguagePreference(languageCode);
                return userRepository.save(user);
            }
            throw new IllegalArgumentException("Invalid language code. Use 'en', 'ms', or 'zh'.");
        }
        throw new RuntimeException("User not found");
    }
    
    // Helper to get profile data
    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
