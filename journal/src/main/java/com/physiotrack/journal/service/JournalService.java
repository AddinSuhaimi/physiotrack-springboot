package com.physiotrack.journal.service;

import com.physiotrack.journal.model.JournalEntry;
import com.physiotrack.journal.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public JournalEntry createEntry(JournalEntry entry) {
        return journalRepository.save(entry);
    }

    public JournalEntry updateEntry(Long id, Long authorId, JournalEntry updates) {
        Optional<JournalEntry> opt = journalRepository.findByIdAndAuthorId(id, authorId);
        if (opt.isPresent()) {
            JournalEntry existing = opt.get();
            if (updates.getTitle() != null) existing.setTitle(updates.getTitle());
            if (updates.getBody() != null) existing.setBody(updates.getBody());
            if (updates.getTags() != null) existing.setTags(updates.getTags());
            if (updates.getSharedWithPhysio() != null) existing.setSharedWithPhysio(updates.getSharedWithPhysio());
            return journalRepository.save(existing);
        }
        throw new RuntimeException("Journal entry not found or not owned by user");
    }

    public void deleteEntry(Long id, Long authorId) {
        Optional<JournalEntry> opt = journalRepository.findByIdAndAuthorId(id, authorId);
        if (opt.isPresent()) {
            journalRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("Journal entry not found or not owned by user");
    }

    public JournalEntry getEntry(Long id, Long authorId) {
        return journalRepository.findByIdAndAuthorId(id, authorId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found or not owned by user"));
    }

    public Page<JournalEntry> listEntries(Long authorId, Pageable pageable) {
        return journalRepository.findByAuthorId(authorId, pageable);
    }
}
