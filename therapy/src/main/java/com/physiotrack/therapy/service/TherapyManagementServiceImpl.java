package com.physiotrack.therapy.service;

import com.physiotrack.therapy.api.TherapyManagementService;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.repository.PTProgramRepository;
import com.physiotrack.therapy.repository.OTProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional  
public class TherapyManagementServiceImpl implements TherapyManagementService {

    private final PTProgramRepository ptProgramRepository;
    private final OTProgramRepository otProgramRepository;

    public TherapyManagementServiceImpl(
            PTProgramRepository ptProgramRepository,
            OTProgramRepository otProgramRepository) {
        this.ptProgramRepository = ptProgramRepository;
        this.otProgramRepository = otProgramRepository;
    }

    @Override
    public void addPTActivity(Long programId, PTActivity activity) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));

        activity.setProgram(program);
        program.getActivities().add(activity);
    }

    @Override
    public void removePTActivity(Long programId, Long activityId) {
        PTProgram program = ptProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("PT Program not found"));

        program.getActivities().removeIf(a -> a.getId().equals(activityId));
    }

    @Override
    public void addOTActivity(Long programId, OTActivity activity) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));

        activity.setProgram(program);
        program.getActivities().add(activity);
    }

    @Override
    public void removeOTActivity(Long programId, Long activityId) {
        OTProgram program = otProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("OT Program not found"));

        program.getActivities().removeIf(a -> a.getId().equals(activityId));
    }
}
