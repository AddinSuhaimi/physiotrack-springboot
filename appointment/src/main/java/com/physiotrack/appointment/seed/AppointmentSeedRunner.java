package com.physiotrack.appointment.seed;

import com.physiotrack.appointment.model.Appointment;
import com.physiotrack.appointment.model.AppointmentRequestType;
import com.physiotrack.appointment.model.AppointmentStatus;
import com.physiotrack.appointment.repository.AppointmentRepository;
import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Order(1) // run before DemoRunner (@Order(2))
@Component
public class AppointmentSeedRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentSeedRunner(UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        seedUsersIfMissing();
        seedAppointmentRequestsIfMissing();
    }

    // -----------------------
    // USERS
    // -----------------------

    private void seedUsersIfMissing() {
        ensureAdmin("admin1", "admin1@demo.com");
        ensurePhysio("physioA", "physioa@demo.com", "Demo Clinic A");
        ensurePhysio("physioB", "physiob@demo.com", "Demo Clinic B");
        ensurePatient("patientX", "patientx@demo.com");
        ensurePatient("patientY", "patienty@demo.com");

        System.out.println("[SEED] Users check completed. Total users: " + userRepository.count());
    }

    private void ensureAdmin(String username, String email) {
        if (existsEmail(email)) return;

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword("Password123");
        u.setRole("ADMIN");
        u.setActive(true);

        userRepository.save(u);
        System.out.println("[SEED] Inserted ADMIN: " + email);
    }

    private void ensurePhysio(String username, String email, String clinicName) {
        if (existsEmail(email)) return;

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword("Password123");
        u.setRole("PHYSIO");
        u.setClinicName(clinicName);
        u.setActive(true);

        userRepository.save(u);
        System.out.println("[SEED] Inserted PHYSIO: " + email);
    }

    private void ensurePatient(String username, String email) {
        if (existsEmail(email)) return;

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword("Password123");
        u.setRole("PATIENT");
        u.setActive(true);

        userRepository.save(u);
        System.out.println("[SEED] Inserted PATIENT: " + email);
    }

    private boolean existsEmail(String email) {
        // UserRepository doesn't expose existsByEmail(...) in your snippet,
        // so use findAll() to keep it compatible with your current repo.
        final String needle = email.toLowerCase(Locale.ROOT);
        return userRepository.findAll().stream()
                .anyMatch(u -> u.getEmail() != null && u.getEmail().toLowerCase(Locale.ROOT).equals(needle));
    }

    private User findAnyByRoleOrThrow(String role) {
        return userRepository.findAll().stream()
                .filter(u -> role.equalsIgnoreCase(u.getRole()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Seed error: no user with role=" + role));
    }

    // -----------------------
    // APPOINTMENTS
    // -----------------------

    private void seedAppointmentRequestsIfMissing() {
        if (appointmentRepository.count() > 0) {
            System.out.println("[SEED] Appointments exist (" + appointmentRepository.count() + "), skipping appointment seeding.");
            return;
        }

        User physio = findAnyByRoleOrThrow("PHYSIO");
        User patient = findAnyByRoleOrThrow("PATIENT");

        Appointment req1 = new Appointment();
        req1.setPatientId(patient.getId());
        req1.setPhysioId(physio.getId());
        req1.setDateTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));
        req1.setDetails("Knee pain, mild swelling");
        req1.setRequestType(AppointmentRequestType.NEW);
        req1.setStatus(AppointmentStatus.PENDING);

        appointmentRepository.save(req1);

        System.out.println("[SEED] Appointment requests seeded (1 pending NEW)");
    }
}
