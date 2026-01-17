package com.physiotrack.summary.api;

import com.physiotrack.summary.model.SummaryReport;

import java.util.List;

public interface SummaryService {
    SummaryReport getMonthlySummary(Long requestingUserId, Long patientId, int month, int year);
    List<SummaryReport> getRecentSummaries(Long requestingUserId, Long patientId);
}
