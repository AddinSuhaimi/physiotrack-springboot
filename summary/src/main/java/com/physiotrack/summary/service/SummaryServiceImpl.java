package com.physiotrack.summary.service;

import com.physiotrack.summary.api.SummaryService;
import com.physiotrack.summary.model.SummaryReport;
import com.physiotrack.summary.repository.SummaryRepository;
import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;
    private final UserManagementService userManagementService;

    public SummaryServiceImpl(SummaryRepository summaryRepository, UserManagementService userManagementService) {
        this.summaryRepository = summaryRepository;
        this.userManagementService = userManagementService;
    }

    @Override
    public SummaryReport getMonthlySummary(Long requestingUserId, Long patientId, int month, int year) {
        User req = userManagementService.getUserById(requestingUserId);

        if (requestingUserId.equals(patientId) || "PHYSIO".equals(req.getRole())) {
            SummaryReport r = summaryRepository.findByPatientIdAndMonthAndYear(patientId, month, year);
            if (r == null) throw new RuntimeException("Summary not found");
            return r;
        }

        throw new RuntimeException("Not authorized to view this summary");
    }

    @Override
    public List<SummaryReport> getRecentSummaries(Long requestingUserId, Long patientId) {
        User req = userManagementService.getUserById(requestingUserId);

        if (requestingUserId.equals(patientId) || "PHYSIO".equals(req.getRole())) {
            return summaryRepository.findByPatientIdOrderByYearDescMonthDesc(patientId);
        }

        throw new RuntimeException("Not authorized to view summaries");
    }
}
