package com.physiotrack.personalinfo.controller;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.personalinfo.service.PersonalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class PersonalInfoController {

    @Autowired
    private PersonalInfoService personalInfoService;

    // Get Profile Details
    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(personalInfoService.getProfile(id));
    }

    // UC04: Update Profile
    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(personalInfoService.updateProfile(id, user));
    }

    // UC05: Change Language
    @PutMapping("/{id}/language")
    public ResponseEntity<User> updateLanguage(@PathVariable Long id, @RequestParam String code) {
        return ResponseEntity.ok(personalInfoService.updateLanguage(id, code));
    }
}
