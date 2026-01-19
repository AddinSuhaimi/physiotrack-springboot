package com.physiotrack.journal.repository;

import com.physiotrack.journal.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
