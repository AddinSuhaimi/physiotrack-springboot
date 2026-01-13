package com.physiotrack.appointment.repository;

import com.physiotrack.appointment.model.Appointment;
import com.physiotrack.appointment.model.AppointmentRequestType;
import com.physiotrack.appointment.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // For admin UC24: list pending requests by type
    List<Appointment> findByRequestTypeAndStatusOrderByCreatedAtDesc(
            AppointmentRequestType requestType,
            AppointmentStatus status
    );

    // For physio UC18: schedule - show approved appointments in a range
    List<Appointment> findByPhysioIdAndStatusAndDateTimeBetweenOrderByDateTimeAsc(
            Long physioId,
            AppointmentStatus status,
            LocalDateTime from,
            LocalDateTime to
    );

    // For availability checks: is there already an approved slot?
    boolean existsByPhysioIdAndStatusAndDateTime(
            Long physioId,
            AppointmentStatus status,
            LocalDateTime dateTime
    );

    // Patient view history (optional)
    List<Appointment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
