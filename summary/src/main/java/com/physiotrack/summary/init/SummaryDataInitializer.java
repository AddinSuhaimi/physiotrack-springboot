package com.physiotrack.summary.init;

import com.physiotrack.summary.model.SummaryReport;
import com.physiotrack.summary.repository.SummaryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(1)
public class SummaryDataInitializer implements CommandLineRunner {

    private final SummaryRepository summaryRepository;

    public SummaryDataInitializer(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        java.time.LocalDate now = LocalDate.now();

        // Ensure there's at least one summary for some seeded patients used in the demo.
        // Seed for patientId=1 if repository empty (legacy seed), and for patientX (id=4) and patientY (id=5) if missing.
        if (summaryRepository.count() == 0) {
            SummaryReport r = new SummaryReport();
            r.setPatientId(1L);
            r.setMonth(now.getMonthValue());
            r.setYear(now.getYear());
            r.setSummaryData("{\"progress\": 75, \"badges\": [\"streak-7\"]}");
            summaryRepository.save(r);
        }

        // Seed for demo patients (patientX id=4, patientY id=5) if they don't already have a summary for this month
        long[] demoPatientIds = new long[] {4L, 5L};
        for (long pid : demoPatientIds) {
            SummaryReport existing = summaryRepository.findByPatientIdAndMonthAndYear(pid, now.getMonthValue(), now.getYear());
            if (existing == null) {
                SummaryReport s = new SummaryReport();
                s.setPatientId(pid);
                s.setMonth(now.getMonthValue());
                s.setYear(now.getYear());
                s.setSummaryData("{\"progress\": 40, \"notes\": \"Demo summary for patient id=" + pid + "\"}");
                SummaryReport saved = summaryRepository.save(s);
                System.out.println("[SUMMARY-SEED] Inserted summary id=" + saved.getId() + " for patientId=" + pid);
            } else {
                System.out.println("[SUMMARY-SEED] Existing summary found for patientId=" + pid + ", id=" + existing.getId());
            }
        }
    }
}
