package com.physiotrack.summary.init;

import com.physiotrack.summary.model.SummaryReport;
import com.physiotrack.summary.repository.SummaryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SummaryDataInitializer implements CommandLineRunner {

    private final SummaryRepository summaryRepository;

    public SummaryDataInitializer(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (summaryRepository.count() == 0) {
            SummaryReport r = new SummaryReport();
            r.setPatientId(1L);
            r.setMonth(LocalDate.now().getMonthValue());
            r.setYear(LocalDate.now().getYear());
            r.setSummaryData("{\"progress\": 75, \"badges\": [\"streak-7\"]}");
            summaryRepository.save(r);
        }
    }
}
