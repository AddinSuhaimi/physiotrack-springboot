package com.physiotrack.appointment.service;

import com.physiotrack.appointment.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {
    List<Appointment> getScheduleForPhysio(Long physioId, LocalDateTime from, LocalDateTime to);
    boolean isSlotAvailable(Long physioId, LocalDateTime dateTime);
}
