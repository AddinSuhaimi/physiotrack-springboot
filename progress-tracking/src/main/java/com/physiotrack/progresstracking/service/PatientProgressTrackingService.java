package com.physiotrack.progresstracking.service;

import com.physiotrack.progresstracking.model.TreatmentReport;
import java.util.List;
import java.util.Optional;

public interface PatientProgressTrackingService {

    TreatmentReport createReport(TreatmentReport report);

    Optional<TreatmentReport> retrieveReport(Long id);

    List<TreatmentReport> retrieveAllReports();

    TreatmentReport updateReport(Long id, TreatmentReport report);

    void deleteReport(Long id);

    List<TreatmentReport> getPatientReports(Long patientId);
}
