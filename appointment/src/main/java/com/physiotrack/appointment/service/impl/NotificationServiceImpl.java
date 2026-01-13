package com.physiotrack.appointment.service.impl;

import com.physiotrack.appointment.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notifyApproval(Long patientId, String message) {
        System.out.println("[NOTIFY] (APP/EMAIL/WHATSAPP) to patientId=" + patientId + " | APPROVED | " + message);
    }

    @Override
    public void notifyRejection(Long patientId, String message) {
        System.out.println("[NOTIFY] (APP/EMAIL/WHATSAPP) to patientId=" + patientId + " | REJECTED | " + message);
    }
}
