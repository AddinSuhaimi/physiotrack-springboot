package com.physiotrack.therapy.service;

import com.physiotrack.therapy.api.TherapyProgressService;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.repository.PTProgramRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.physiotrack.therapy.repository.OTProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TherapyProgressServiceImpl implements TherapyProgressService {

    private final PTProgramRepository ptProgramRepository;
    private final OTProgramRepository otProgramRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TherapyProgressServiceImpl(
            PTProgramRepository ptProgramRepository,
            OTProgramRepository otProgramRepository) {
        this.ptProgramRepository = ptProgramRepository;
        this.otProgramRepository = otProgramRepository;
    }
    
    @Override
    @Transactional (readOnly = true)
    public List<PTActivity> getPTActivities(Long programId) {
        entityManager.clear(); // Force reload from DB
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));
        return program.getActivities();
    }


    @Override
    @Transactional
    public void markPTCompleted(Long ptProgramId, Long activityId) {
        PTProgram program = ptProgramRepository.findById(ptProgramId)
        .orElseThrow(() -> new RuntimeException("PT Program not found"));

        PTActivity activity = program.getActivities().stream()
        .filter(a -> a.getId().equals(activityId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setCompleted(true);
        ptProgramRepository.saveAndFlush(program); 

        entityManager.clear();
}

    @Override
    @Transactional (readOnly = true)
    public List<OTActivity> getOTActivities(Long programId) {
        entityManager.clear(); // Force reload from DB
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));
        return program.getActivities();
    }


    @Override
    @Transactional
    public void markOTCompleted(Long otProgramId, Long activityId) {
        OTProgram program = otProgramRepository.findById(otProgramId)
        .orElseThrow(() -> new RuntimeException("OT Program not found"));

        OTActivity activity = program.getActivities().stream()
        .filter(a -> a.getId().equals(activityId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Activity not found"));

        activity.setCompleted(true);
        otProgramRepository.saveAndFlush(program); 

        entityManager.clear();
}
}
