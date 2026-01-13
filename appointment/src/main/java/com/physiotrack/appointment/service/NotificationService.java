package com.physiotrack.appointment.service;

public interface NotificationService {
    void notifyApproval(Long patientId, String message);
    void notifyRejection(Long patientId, String message);
}
