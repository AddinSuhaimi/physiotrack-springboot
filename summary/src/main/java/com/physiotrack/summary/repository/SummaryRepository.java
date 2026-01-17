package com.physiotrack.summary.repository;

import com.physiotrack.summary.model.SummaryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<SummaryReport, Long> {
    List<SummaryReport> findByPatientIdOrderByYearDescMonthDesc(Long patientId);
    SummaryReport findByPatientIdAndMonthAndYear(Long patientId, int month, int year);
}
