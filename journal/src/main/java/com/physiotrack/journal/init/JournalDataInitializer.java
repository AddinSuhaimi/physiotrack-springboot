package com.physiotrack.journal.init;

import com.physiotrack.journal.model.Journal;
import com.physiotrack.journal.repository.JournalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JournalDataInitializer implements CommandLineRunner {

    private final JournalRepository journalRepository;

    public JournalDataInitializer(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (journalRepository.count() == 0) {
            Journal j = new Journal();
            j.setPatientId(1L);
            j.setTitle("First entry");
            j.setWeather("Sunny");
            j.setFeeling("Good");
            j.setHealthCondition("Stable");
            j.setComment("Seeded journal entry.");
            j.setImageUrl(null);
            j.setSharedWithPhysio(false);
            j.setCreatedAt(LocalDateTime.now().minusDays(1));
            j.setUpdatedAt(LocalDateTime.now().minusDays(1));
            journalRepository.save(j);
        }
    }
}
