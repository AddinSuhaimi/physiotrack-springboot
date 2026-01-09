package com.physiotrack.therapy.service;

import com.physiotrack.therapy.api.TherapyProgressService;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.repository.PTProgramRepository;
import com.physiotrack.therapy.repository.OTProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional (readOnly = true)
public class TherapyProgressServiceImpl implements TherapyProgressService {

    private final PTProgramRepository ptProgramRepository;
    private final OTProgramRepository otProgramRepository;

    public TherapyProgressServiceImpl(
            PTProgramRepository ptProgramRepository,
            OTProgramRepository otProgramRepository) {
        this.ptProgramRepository = ptProgramRepository;
        this.otProgramRepository = otProgramRepository;
    }

    @Override
    public List<PTActivity> getPTActivities(Long programId) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));

        program.getActivities().size();   // 🔴 FORCE INITIALIZATION
        return program.getActivities();
    }


    @Override
    public void markPTCompleted(Long programId, Long activityId) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));

        program.getActivities().stream()
                .filter(a -> a.getId().equals(activityId))
                .findFirst()
                .ifPresent(a -> a.setCompleted(true));
    }

    @Override
    public List<OTActivity> getOTActivities(Long programId) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));

        program.getActivities().size();   // 🔴 FORCE INITIALIZATION
        return program.getActivities();
    }


    @Override
    public void markOTCompleted(Long programId, Long activityId) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));

        program.getActivities().stream()
                .filter(a -> a.getId().equals(activityId))
                .findFirst()
                .ifPresent(a -> a.setCompleted(true));
    }
}
