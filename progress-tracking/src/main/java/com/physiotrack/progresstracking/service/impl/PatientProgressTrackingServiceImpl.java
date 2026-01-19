package com.physiotrack.progresstracking.service.impl;

import com.physiotrack.progresstracking.model.TreatmentReport;
import com.physiotrack.progresstracking.repository.TreatmentReportRepository;
import com.physiotrack.progresstracking.service.PatientProgressTrackingService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PatientProgressTrackingServiceImpl implements PatientProgressTrackingService {

    private final TreatmentReportRepository treatmentReportRepository;

    public PatientProgressTrackingServiceImpl(TreatmentReportRepository treatmentReportRepository) {
        this.treatmentReportRepository = treatmentReportRepository;
    }

    @Override
    public TreatmentReport createReport(TreatmentReport report) {
        return treatmentReportRepository.save(report);
    }

    @Override
    public Optional<TreatmentReport> retrieveReport(Long id) {
        return treatmentReportRepository.findById(id);
    }

    @Override
    public List<TreatmentReport> retrieveAllReports() {
        return treatmentReportRepository.findAll();
    }

    @Override
    public TreatmentReport updateReport(Long id, TreatmentReport updatedReport) {
        return treatmentReportRepository.findById(id)
                .map(report -> {
                    report.setReportTitle(updatedReport.getReportTitle());
                    report.setDateTime(updatedReport.getDateTime());
                    report.setReportType(updatedReport.getReportType());
                    report.setPerformance(updatedReport.getPerformance());
                    report.setActivity(updatedReport.getActivity());
                    return treatmentReportRepository.save(report);
                })
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    @Override
    public void deleteReport(Long id) {
        treatmentReportRepository.deleteById(id);
    }

    @Override
    public List<TreatmentReport> getPatientReports(Long patientId) {
        return treatmentReportRepository.findByPatientId(patientId);
    }
}
