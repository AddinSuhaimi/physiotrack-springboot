package com.physiotrack.personalinfo.service.impl;

import com.physiotrack.personalinfo.service.PersonalInfoService;
import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonalInfoServiceImpl implements PersonalInfoService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User updateProfile(Long userId, User updatedData) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();

            if (updatedData.getUsername() != null) existingUser.setUsername(updatedData.getUsername());
            if (updatedData.getPhone() != null) existingUser.setPhone(updatedData.getPhone());
            if (updatedData.getAddress() != null) existingUser.setAddress(updatedData.getAddress());
            if (updatedData.getGender() != null) existingUser.setGender(updatedData.getGender());
            if (updatedData.getProfileImageUrl() != null) existingUser.setProfileImageUrl(updatedData.getProfileImageUrl());

            return userRepository.save(existingUser);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    @Override
    public User updateLanguage(Long userId, String languageCode) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (languageCode.equals("en") || languageCode.equals("ms") || languageCode.equals("zh")) {
                user.setLanguagePreference(languageCode);
                return userRepository.save(user);
            }
            throw new IllegalArgumentException("Invalid language code.");
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public User getProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
