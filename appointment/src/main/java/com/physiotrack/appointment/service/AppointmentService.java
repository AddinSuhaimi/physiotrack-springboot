package com.physiotrack.appointment.service;

import com.physiotrack.appointment.model.Appointment;

import java.util.List;

public interface AppointmentService {
    // UC15 patient booking request
    Appointment createBookingRequest(Long patientId, Long physioId, java.time.LocalDateTime dateTime, String details);

    // UC16 update request
    Appointment createUpdateRequest(Long patientId, Long targetAppointmentId, java.time.LocalDateTime newDateTime, String newDetails);

    // UC17 cancel request
    Appointment createCancelRequest(Long patientId, Long targetAppointmentId);

    // UC24 admin manage requests
    List<Appointment> listPendingNewRequests();
    List<Appointment> listPendingUpdateRequests();
    List<Appointment> listPendingCancelRequests();

    Appointment approveRequest(Long requestId);
    Appointment rejectRequest(Long requestId);

    // Confirmed NEW appointments that represent “booked appointments”
    List<Appointment> listConfirmedAppointmentsForPatient(Long patientId);

    // Optional utility for selecting target appointments safely
    Appointment getAppointmentById(Long appointmentId);
}
