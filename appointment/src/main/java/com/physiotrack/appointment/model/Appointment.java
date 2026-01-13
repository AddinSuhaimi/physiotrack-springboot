package com.physiotrack.appointment.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Patient who requested
    @Column(nullable = false)
    private Long patientId;

    // Physiotherapist selected during booking
    @Column(nullable = false)
    private Long physioId;

    // Appointment date/time requested (or updated)
    @Column(nullable = false)
    private LocalDateTime dateTime;

    // Notes/symptoms/details
    @Column(length = 500)
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentRequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    // For UPDATE/CANCEL: which approved appointment is being updated/cancelled
    private Long targetAppointmentId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ----- getters/setters -----
    public Long getId() { return id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getPhysioId() { return physioId; }
    public void setPhysioId(Long physioId) { this.physioId = physioId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public AppointmentRequestType getRequestType() { return requestType; }
    public void setRequestType(AppointmentRequestType requestType) { this.requestType = requestType; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public Long getTargetAppointmentId() { return targetAppointmentId; }
    public void setTargetAppointmentId(Long targetAppointmentId) { this.targetAppointmentId = targetAppointmentId; }
}
