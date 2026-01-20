package com.physiotrack.progresstracking.service.impl;

import com.physiotrack.progresstracking.model.TreatmentReport;
import com.physiotrack.progresstracking.repository.TreatmentReportRepository;
import com.physiotrack.progresstracking.service.PatientProgressTrackingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class PatientProgressTrackingServiceImpl implements PatientProgressTrackingService {

    private final TreatmentReportRepository treatmentReportRepository;

    public PatientProgressTrackingServiceImpl(TreatmentReportRepository treatmentReportRepository) {
        this.treatmentReportRepository = treatmentReportRepository;
    }

    @Override
    public TreatmentReport createReport(
        String title, 
        String type, 
        String activity, 
        int performanceScore, 
        LocalDateTime dateTime, 
        Long patientId
    ) {
        TreatmentReport report = new TreatmentReport();
        report.setReportTitle(title);
        report.setReportType(type);
        report.setActivity(activity);
        report.setPerformance(performanceScore);
        report.setDateTime(dateTime);
        report.setPatientId(patientId);
        return treatmentReportRepository.save(report);
    }

    @Override
    public List<TreatmentReport> getPatientReports(Long patientId) {
        return treatmentReportRepository.findByPatientId(patientId);
    }
}
