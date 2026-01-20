package com.physiotrack.progresstracking.service;

import com.physiotrack.progresstracking.model.TreatmentReport;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientProgressTrackingService {

    TreatmentReport createReport(
        String title, 
        String type, 
        String activity, 
        int performanceScore, 
        LocalDateTime dateTime, 
        Long patientId
    );

    List<TreatmentReport> getPatientReports(Long patientId);
}
