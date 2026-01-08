package com.physiotrack.personalinfo.service;

import com.physiotrack.usermanagement.model.User;

public interface PersonalInfoService {
    User updateProfile(Long userId, User updatedData);
    User updateLanguage(Long userId, String languageCode);
    User getProfile(Long userId);
}