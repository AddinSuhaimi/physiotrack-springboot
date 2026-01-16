package com.physiotrack.appointment.service.impl;

import com.physiotrack.appointment.model.*;
import com.physiotrack.appointment.repository.AppointmentRepository;
import com.physiotrack.appointment.service.AppointmentService;
import com.physiotrack.appointment.service.NotificationService;
import com.physiotrack.appointment.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleService scheduleService;
    private final NotificationService notificationService;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            ScheduleService scheduleService,
            NotificationService notificationService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleService = scheduleService;
        this.notificationService = notificationService;
    }

    @Override
    public Appointment createBookingRequest(Long patientId, Long physioId, LocalDateTime dateTime, String details) {
        // UC15 validation: timeslot chosen
        if (dateTime == null) throw new IllegalArgumentException("Please choose a timeslot.");
        if (!scheduleService.isSlotAvailable(physioId, dateTime)) {
            throw new IllegalArgumentException("Timeslot already taken for this physiotherapist.");
        }

        Appointment req = new Appointment();
        req.setPatientId(patientId);
        req.setPhysioId(physioId);
        req.setDateTime(dateTime);
        req.setDetails(details);
        req.setRequestType(AppointmentRequestType.NEW);
        req.setStatus(AppointmentStatus.PENDING);

        return appointmentRepository.save(req);
    }

    @Override
    public Appointment createUpdateRequest(Long patientId, Long targetAppointmentId, LocalDateTime newDateTime, String newDetails) {
        if (targetAppointmentId == null) throw new IllegalArgumentException("Target appointment is required.");
        if (newDateTime == null) throw new IllegalArgumentException("Please choose a timeslot.");

        Appointment req = new Appointment();
        req.setPatientId(patientId);

        Appointment target = appointmentRepository.findById(targetAppointmentId)
                .orElseThrow(() -> new RuntimeException("Target appointment not found: " + targetAppointmentId));

        req.setPhysioId(target.getPhysioId());

        if (!scheduleService.isSlotAvailable(target.getPhysioId(), newDateTime)) {
            throw new IllegalArgumentException("New timeslot already taken for this physiotherapist.");
        }

        req.setDateTime(newDateTime);
        req.setDetails(newDetails);
        req.setRequestType(AppointmentRequestType.UPDATE);
        req.setStatus(AppointmentStatus.PENDING);
        req.setTargetAppointmentId(targetAppointmentId);

        return appointmentRepository.save(req);
    }

    @Override
    public Appointment createCancelRequest(Long patientId, Long targetAppointmentId) {
        if (targetAppointmentId == null) throw new IllegalArgumentException("Target appointment is required.");

        Appointment target = appointmentRepository.findById(targetAppointmentId)
                .orElseThrow(() -> new RuntimeException("Target appointment not found: " + targetAppointmentId));

        Appointment req = new Appointment();
        req.setPatientId(patientId);
        req.setPhysioId(target.getPhysioId());
        req.setDateTime(target.getDateTime());
        req.setRequestType(AppointmentRequestType.CANCEL);
        req.setStatus(AppointmentStatus.PENDING);
        req.setTargetAppointmentId(targetAppointmentId);

        return appointmentRepository.save(req);
    }

    @Override
    public List<Appointment> listPendingNewRequests() {
        return appointmentRepository.findByRequestTypeAndStatusOrderByCreatedAtDesc(
                AppointmentRequestType.NEW, AppointmentStatus.PENDING
        );
    }

    @Override
    public List<Appointment> listPendingUpdateRequests() {
        return appointmentRepository.findByRequestTypeAndStatusOrderByCreatedAtDesc(
                AppointmentRequestType.UPDATE, AppointmentStatus.PENDING
        );
    }

    @Override
    public List<Appointment> listPendingCancelRequests() {
        return appointmentRepository.findByRequestTypeAndStatusOrderByCreatedAtDesc(
                AppointmentRequestType.CANCEL, AppointmentStatus.PENDING
        );
    }

    @Override
    public Appointment approveRequest(Long requestId) {
        Appointment req = appointmentRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        req.setStatus(AppointmentStatus.APPROVED);
        Appointment saved = appointmentRepository.save(req);

        notificationService.notifyApproval(req.getPatientId(),
                "Your appointment request (" + req.getRequestType() + ") has been approved.");

        return saved;
    }

    @Override
    public Appointment rejectRequest(Long requestId) {
        Appointment req = appointmentRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found: " + requestId));

        req.setStatus(AppointmentStatus.REJECTED);
        Appointment saved = appointmentRepository.save(req);

        notificationService.notifyRejection(req.getPatientId(),
                "Your appointment request (" + req.getRequestType() + ") has been rejected.");

        return saved;
    }

    @Override
    public List<Appointment> listConfirmedAppointmentsForPatient(Long patientId) {
        if (patientId == null) return Collections.emptyList();

        // Uses existing repository method
        return appointmentRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                // “booked/confirmed appointment” = an approved NEW appointment
                .filter(a -> a.getRequestType() == AppointmentRequestType.NEW)
                .filter(a -> a.getStatus() == AppointmentStatus.APPROVED)
                .toList();
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
    }

}
