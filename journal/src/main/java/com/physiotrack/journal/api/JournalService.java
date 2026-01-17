package com.physiotrack.journal.api;

import com.physiotrack.journal.model.Journal;

import java.util.List;

public interface JournalService {
    Journal createJournal(Long requestingUserId, Journal journal);
    List<Journal> getJournalsForPatient(Long requestingUserId, Long patientId);
    Journal getJournalById(Long requestingUserId, Long journalId);
    Journal updateJournal(Long requestingUserId, Long journalId, Journal updated);
    void deleteJournal(Long requestingUserId, Long journalId);
    Journal setSharedWithPhysio(Long requestingUserId, Long journalId, boolean shared);
}
