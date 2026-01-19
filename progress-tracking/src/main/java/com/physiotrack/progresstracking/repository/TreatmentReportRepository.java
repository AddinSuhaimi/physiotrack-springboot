package com.physiotrack.progresstracking.repository;

import com.physiotrack.progresstracking.model.TreatmentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreatmentReportRepository extends JpaRepository<TreatmentReport, Long> {
    // JpaRepository provides save, findAll, findById, deleteById
    List<TreatmentReport> findByPatientId(Long patientId);

}
