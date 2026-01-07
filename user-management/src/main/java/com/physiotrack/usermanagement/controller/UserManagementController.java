package com.physiotrack.usermanagement.controller;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserManagementController {

    @Autowired
    private UserManagementService userService;

    // UC25: Register Physio
    @PostMapping("/physiotherapists")
    public ResponseEntity<User> registerPhysio(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerPhysiotherapist(user));
    }

    // UC26: View Users (Optional filter by role)
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }

    // UC27: Deactivate User
    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }
}
