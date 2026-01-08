package com.physiotrack.usermanagement.service;

import com.physiotrack.usermanagement.model.User;
import java.util.List;

public interface UserManagementService {
    User registerPhysiotherapist(User user);
    List<User> getAllUsers(String role);
    User deactivateUser(Long userId);
    User getUserById(Long userId); 
}

