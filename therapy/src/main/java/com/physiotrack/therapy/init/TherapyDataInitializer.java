package com.physiotrack.therapy.init;

import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.repository.PTProgramRepository;
import com.physiotrack.therapy.repository.OTProgramRepository;
import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(2) // runs AFTER AppointmentSeedRunner (@Order(1))
@Component
public class TherapyDataInitializer implements CommandLineRunner {

    private final PTProgramRepository ptRepo;
    private final OTProgramRepository otRepo;
    private final UserRepository userRepo;

    public TherapySeedRunner(
            PTProgramRepository ptRepo,
            OTProgramRepository otRepo,
            UserRepository userRepo) {
        this.ptRepo = ptRepo;
        this.otRepo = otRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        seedTherapyForPatients();
    }

    private void seedTherapyForPatients() {
        List<User> patients = userRepo.findAll().stream()
                .filter(u -> "PATIENT".equalsIgnoreCase(u.getRole()))
                .toList();

        for (User patient : patients) {
            seedPT(patient.getId());
            seedOT(patient.getId());
        }

        System.out.println("[SEED] Therapy programs and activities seeded for all patients.");
    }

    private void seedPT(Long patientId) {
        if (ptRepo.findByPatientId(patientId) != null) {
            System.out.println("[SEED] PTProgram already exists for patientId=" + patientId);
            return;
        }

        PTProgram program = new PTProgram();
        program.setPatientId(patientId);
        program = ptRepo.save(program);

        System.out.println("[SEED] Inserted PTProgram for patientId=" + patientId
                + ", programId=" + program.getId());

        PTActivity a1 = new PTActivity();
        a1.setName("Knee Flexion Exercise");
        a1.setCompleted(false);
        a1.setProgram(program);

        PTActivity a2 = new PTActivity();
        a2.setName("Quad Sets");
        a2.setCompleted(false);
        a2.setProgram(program);

        program.getActivities().add(a1);
        program.getActivities().add(a2);

        ptRepo.save(program);

        System.out.println("[SEED] Added PT activities for patientId=" + patientId);
    }

    private void seedOT(Long patientId) {
        if (otRepo.findByPatientId(patientId) != null) {
            System.out.println("[SEED] OTProgram already exists for patientId=" + patientId);
            return;
        }

        OTProgram program = new OTProgram();
        program.setPatientId(patientId);
        program = otRepo.save(program);

        System.out.println("[SEED] Inserted OTProgram for patientId=" + patientId
                + ", programId=" + program.getId());

        OTActivity a1 = new OTActivity();
        a1.setName("Hand Stretch");
        a1.setCompleted(false);
        a1.setProgram(program);

        OTActivity a2 = new OTActivity();
        a2.setName("Grip Strength");
        a2.setCompleted(false);
        a2.setProgram(program);

        program.getActivities().add(a1);
        program.getActivities().add(a2);

        otRepo.save(program);

        System.out.println("[SEED] Added OT activities for patientId=" + patientId);
    }
}
