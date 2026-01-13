package com.physiotrack.therapy.init;

import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.repository.PTProgramRepository;
import com.physiotrack.therapy.repository.OTProgramRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TherapyDataInitializer implements CommandLineRunner {

    private final PTProgramRepository ptRepo;
    private final OTProgramRepository otRepo;

    public TherapyDataInitializer(
            PTProgramRepository ptRepo,
            OTProgramRepository otRepo) {
        this.ptRepo = ptRepo;
        this.otRepo = otRepo;
    }

    public PTProgram createPTProgram(Long patientId) {
        PTProgram program = new PTProgram();
        program.setPatientId(patientId);
        program.setAssignedDate(LocalDate.now());
        return ptRepo.save(program);
    }

    public OTProgram createOTProgram(Long patientId) {
        OTProgram program = new OTProgram();
        program.setPatientId(patientId);
        program.setAssignedDate(LocalDate.now());
        return otRepo.save(program);
    }

    @Override
    public void run(String... args) {
        PTProgram pt = new PTProgram();
        pt.setPatientId(1L);
        pt.setAssignedDate(LocalDate.now());
        ptRepo.save(pt);

        OTProgram ot = new OTProgram();
        ot.setPatientId(1L);
        ot.setAssignedDate(LocalDate.now());
        otRepo.save(ot);
    }
}
