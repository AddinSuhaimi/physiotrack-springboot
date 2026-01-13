package com.physiotrack.appointment.service.impl;

import com.physiotrack.appointment.model.Appointment;
import com.physiotrack.appointment.model.AppointmentStatus;
import com.physiotrack.appointment.repository.AppointmentRepository;
import com.physiotrack.appointment.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final AppointmentRepository appointmentRepository;

    public ScheduleServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> getScheduleForPhysio(Long physioId, LocalDateTime from, LocalDateTime to) {
        return appointmentRepository.findByPhysioIdAndStatusAndDateTimeBetweenOrderByDateTimeAsc(
                physioId, AppointmentStatus.APPROVED, from, to
        );
    }

    @Override
    public boolean isSlotAvailable(Long physioId, LocalDateTime dateTime) {
        return !appointmentRepository.existsByPhysioIdAndStatusAndDateTime(
                physioId, AppointmentStatus.APPROVED, dateTime
        );
    }
}
