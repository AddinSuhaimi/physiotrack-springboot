package com.physiotrack.journal.service;

import com.physiotrack.journal.api.JournalService;
import com.physiotrack.journal.model.Journal;
import com.physiotrack.journal.repository.JournalRepository;
import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalServiceImpl implements JournalService {

    private final JournalRepository journalRepository;
    private final UserManagementService userManagementService;

    public JournalServiceImpl(JournalRepository journalRepository, UserManagementService userManagementService) {
        this.journalRepository = journalRepository;
        this.userManagementService = userManagementService;
    }

    @Override
    @Transactional
    public Journal createJournal(Long requestingUserId, Journal journal) {
        // ensure the requesting user exists and is the owner (patient)
        User reqUser = userManagementService.getUserById(requestingUserId);
        if (!requestingUserId.equals(journal.getPatientId()) && !"PHYSIO".equals(reqUser.getRole())) {
            throw new RuntimeException("Not authorized to create journal for this patient");
        }

        journal.setCreatedAt(LocalDateTime.now());
        journal.setUpdatedAt(LocalDateTime.now());
        return journalRepository.save(journal);
    }

    @Override
    public List<Journal> getJournalsForPatient(Long requestingUserId, Long patientId) {
        User reqUser = userManagementService.getUserById(requestingUserId);

        // patient can see their own journals; physio can see only shared ones
        if (requestingUserId.equals(patientId) || "PHYSIO".equals(reqUser.getRole())) {
            List<Journal> all = journalRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
            if (requestingUserId.equals(patientId)) return all;
            // filter for physio only shared journals
            return all.stream().filter(Journal::isSharedWithPhysio).toList();
        }

        throw new RuntimeException("Not authorized to view journals for this patient");
    }

    @Override
    public Journal getJournalById(Long requestingUserId, Long journalId) {
        Journal j = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        User reqUser = userManagementService.getUserById(requestingUserId);
        if (requestingUserId.equals(j.getPatientId())) return j;
        if ("PHYSIO".equals(reqUser.getRole()) && j.isSharedWithPhysio()) return j;

        throw new RuntimeException("Not authorized to view this journal");
    }

    @Override
    @Transactional
    public Journal updateJournal(Long requestingUserId, Long journalId, Journal updated) {
        Journal existing = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!requestingUserId.equals(existing.getPatientId())) {
            throw new RuntimeException("Only owner can update the journal");
        }

        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getWeather() != null) existing.setWeather(updated.getWeather());
        if (updated.getFeeling() != null) existing.setFeeling(updated.getFeeling());
        if (updated.getHealthCondition() != null) existing.setHealthCondition(updated.getHealthCondition());
        if (updated.getComment() != null) existing.setComment(updated.getComment());
        if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());

        existing.setUpdatedAt(LocalDateTime.now());
        return journalRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteJournal(Long requestingUserId, Long journalId) {
        Journal existing = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!requestingUserId.equals(existing.getPatientId())) {
            throw new RuntimeException("Only owner can delete the journal");
        }

        journalRepository.delete(existing);
    }

    @Override
    @Transactional
    public Journal setSharedWithPhysio(Long requestingUserId, Long journalId, boolean shared) {
        Journal existing = journalRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (!requestingUserId.equals(existing.getPatientId())) {
            throw new RuntimeException("Only owner can change sharing settings");
        }

        existing.setSharedWithPhysio(shared);
        existing.setUpdatedAt(LocalDateTime.now());
        return journalRepository.save(existing);
    }
}
