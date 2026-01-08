package com.physiotrack.journal.repository;

import com.physiotrack.journal.model.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    Page<JournalEntry> findByAuthorId(Long authorId, Pageable pageable);
    Optional<JournalEntry> findByIdAndAuthorId(Long id, Long authorId);
}
