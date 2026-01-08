package com.physiotrack.therapy.service;

import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.repository.PTProgramRepository;
import com.physiotrack.therapy.repository.OTProgramRepository;
import org.springframework.stereotype.Service;

@Service
public class TherapyManagementService {

    private final PTProgramRepository ptProgramRepository;
    private final OTProgramRepository otProgramRepository;

    public TherapyManagementService(
            PTProgramRepository ptProgramRepository,
            OTProgramRepository otProgramRepository) {
        this.ptProgramRepository = ptProgramRepository;
        this.otProgramRepository = otProgramRepository;
    }

    // UC20 – Modify physiotherapy activities
    public void addPTActivity(Long programId, PTActivity activity) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));
        program.getActivities().add(activity);
        ptProgramRepository.save(program);
    }

    public void removePTActivity(Long programId, Long activityId) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));
        program.getActivities().removeIf(a -> a.getId().equals(activityId));
        ptProgramRepository.save(program);
    }

    // UC21 – Modify occupational therapy activities
    public void addOTActivity(Long programId, OTActivity activity) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));
        program.getActivities().add(activity);
        otProgramRepository.save(program);
    }

    public void removeOTActivity(Long programId, Long activityId) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));
        program.getActivities().removeIf(a -> a.getId().equals(activityId));
        otProgramRepository.save(program);
    }
}
