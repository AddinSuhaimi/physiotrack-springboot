package com.physiotrack.usermanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username; // Changed from 'name' to match Flutter

    @Column(nullable = false)
    private String role; // "PATIENT", "PHYSIO", "ADMIN"

    // Fields required by Flutter App
    private LocalDateTime createTime;
    private Boolean isTakenTest = false;
    private String address;
    private String phone;
    private String profileImageUrl;
    private Integer level = 1;
    private Integer totalExp = 0;
    private Double progressToNextLevel = 0.0;
    private Boolean sharedJournal = false;
    private String gender;

    // Backend specific fields
    private String password; 
    private boolean isActive = true; 
    private String languagePreference = "en"; 
    private String clinicName; // For Physios

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}