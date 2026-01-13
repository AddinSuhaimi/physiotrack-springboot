package com.physiotrack.springboot_app;

import java.util.List;
import java.util.Scanner;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;
import com.physiotrack.personalinfo.service.PersonalInfoService;
import com.physiotrack.usermanagement.repository.UserRepository;

// Therapy module imports (provided by teammate)
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.api.TherapyManagementService;
import com.physiotrack.therapy.api.TherapyProgressService;
import com.physiotrack.therapy.init.TherapyDataInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * INTERACTIVE DemoRunner (Console Menu)
 *
 * - Shows a menu of modules
 * - Prompts user to select a module to run
 * - Loops until user exits
 *
 * Note:
 * - This class should NOT contain business logic. It should only call services and print outputs.
 */
@Component
public class DemoRunner implements CommandLineRunner {

  // ADD HERE AFTER DONE
  private final UserManagementService userManagementService;
  private final PersonalInfoService personalInfoService;

  private final TherapyManagementService therapyManagementService;
  private final TherapyProgressService therapyProgressService;
  private final TherapyDataInitializer therapyDataInitializer;

  // Temporary direct repo access for seeding/demo data
  private final UserRepository userRepository;

  // ADD HERE AFTER DONE
  @Autowired
  public DemoRunner(
      UserManagementService userManagementService,
      PersonalInfoService personalInfoService,
      TherapyManagementService therapyManagementService,
      TherapyProgressService therapyProgressService,
      UserRepository userRepository,
      TherapyDataInitializer therapyDataInitializer
  ) {
    this.userManagementService = userManagementService;
    this.personalInfoService = personalInfoService;
    this.therapyManagementService = therapyManagementService;
    this.therapyProgressService = therapyProgressService;
    this.userRepository = userRepository;
    this.therapyDataInitializer = therapyDataInitializer;
  }

  @Override
  public void run(String... args) {
    System.out.println("=======================================");
    System.out.println("[DEMO] PhysioTrack Spring Boot started");
    System.out.println("=======================================");

    // IMPORTANT: don't close System.in (closing Scanner will close System.in)
    Scanner scanner = new Scanner(System.in);

    boolean running = true;
    while (running) {
      printMainMenu();

      int choice = readInt(scanner, "Select a module (0 to Exit): ");

      switch (choice) {
        case 0:
          running = false;
          break;

        case 1:
          demoUserManagement();
          break;

        case 2:
          demoPersonalInfo();
          break;

        case 3:
          demoAppointmentPlaceholder();
          break;

        case 4:
          demoNotificationPlaceholder();
          break;

        case 5:
          demoPhysiotherapy(); // subset of therapy demo focused on PT
          break;

        case 6:
          demoOccupationalTherapy(); // subset of therapy demo focused on OT
          break;

        case 7:
          demoFirstTimeScreeningPlaceholder();
          break;

        case 8:
          demoProgressTrackingPlaceholder();
          break;

        case 9:
          demoJournalPlaceholder();
          break;

        case 10:
          demoSummaryPlaceholder();
          break;

        default:
          System.out.println("[ERROR] Invalid selection. Please choose 0-10.");
      }

      if (running) {
        System.out.println("\n---------------------------------------");
        System.out.println("Press ENTER to return to main menu...");
        scanner.nextLine(); // consume pending newline if any
        scanner.nextLine(); // wait for enter
      }
    }

    System.out.println("\n==========================================");
    System.out.println("   DEMO TERMINATED");
    System.out.println("==========================================");
  }

  // =========================
  // MENU
  // =========================
  private void printMainMenu() {
    System.out.println("\n============== MAIN MENU ==============");
    System.out.println(" 1) User Management Module");
    System.out.println(" 2) Manage User Personal Information");
    System.out.println(" 3) Appointment Booking Module");
    System.out.println(" 4) Notification Pushing Module");
    System.out.println(" 5) Physiotherapy Module");
    System.out.println(" 6) Occupational Therapy Module");
    System.out.println(" 7) First Time Screening Module");
    System.out.println(" 8) Patient Progress Tracking Module");
    System.out.println(" 9) Manage Journal Module");
    System.out.println("10) Summary Report Module");
    System.out.println(" 0) Exit");
    System.out.println("======================================");
  }

  private int readInt(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt);
      String line = scanner.nextLine().trim();
      try {
        return Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("[ERROR] Please enter a number.");
      }
    }
  }

  // =========================
  // MODULE DEMOS
  // =========================

  // --- 1) User Management Module ---
  private void demoUserManagement() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] USER MANAGEMENT MODULE DEMO");
    System.out.println("==========================================");

    System.out.println("\n[TEST] UC25: Registering Physiotherapist Account...");
    User newPhysio = new User();
    newPhysio.setUsername("JamesPhysio");
    newPhysio.setEmail("james@physio.com");
    newPhysio.setPassword("SecurePass123");
    newPhysio.setClinicName("Sunway Medical");

    try {
      User savedPhysio = userManagementService.registerPhysiotherapist(newPhysio);
      System.out.println("   -> SUCCESS: Physio Registered. ID: " + savedPhysio.getId() + ", Role: " + savedPhysio.getRole());
    } catch (Exception e) {
      System.out.println("   -> FAILED: " + e.getMessage());
    }

    System.out.println("\n[TEST] UC26: Viewing Registered Users...");
    List<User> allUsers = userManagementService.getAllUsers(null);
    for (User u : allUsers) {
      System.out.println("   -> User: " + u.getUsername() + " | Role: " + u.getRole() + " | Active: " + u.isActive());
    }

    System.out.println("\n[TEST] UC27: Admin Deactivates User (User ID=1)...");
    try {
      User deactivatedUser = userManagementService.deactivateUser(1L);
      System.out.println("   -> SUCCESS: User status is now Active? " + deactivatedUser.isActive());
    } catch (Exception e) {
      System.out.println("   -> FAILED: " + e.getMessage());
    }
  }

  // --- 2) Manage User Personal Information ---
  private void demoPersonalInfo() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] PERSONAL INFO MODULE DEMO");
    System.out.println("==========================================");

    System.out.println("\n[TEST] UC04: User Updates Profile (User ID=1)...");
    try {
      User updateData = new User();
      updateData.setPhone("+60123456789");
      updateData.setAddress("Kuala Lumpur, Malaysia");

      User updatedUser = personalInfoService.updateProfile(1L, updateData);
      System.out.println("   -> SUCCESS: Profile Updated.");
      System.out.println("      New Phone: " + updatedUser.getPhone());
      System.out.println("      New Address: " + updatedUser.getAddress());
    } catch (Exception e) {
      System.out.println("   -> FAILED: " + e.getMessage());
    }

    System.out.println("\n[TEST] UC05: User Changes Language Preference (User ID=1)...");
    try {
      User langUser = personalInfoService.updateLanguage(1L, "ms");
      System.out.println("   -> SUCCESS: Language changed to: " + langUser.getLanguagePreference());
    } catch (Exception e) {
      System.out.println("   -> FAILED: " + e.getMessage());
    }
  }

  // --- 5) Physiotherapy Module (PT-focused demo) ---
  private void demoPhysiotherapy() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] PHYSIOTHERAPY MODULE DEMO (PT)");
    System.out.println("==========================================");

    User physio = findAnyPhysioOrThrow();

    User patientA = seedPatient("patientA", "patientA@test.com");

    PTProgram ptProgramA = therapyDataInitializer.createPTProgram(patientA.getId());
    Long ptProgramAId = ptProgramA.getId();

    System.out.println("\n[TEST] UC20: Modify Patient’s Physiotherapy Activities...");
    PTActivity ptActivity = new PTActivity();
    ptActivity.setName("Arm Stretch");
    ptActivity.setDescription("Stretch arms slowly for 5 minutes");

    therapyManagementService.addPTActivity(ptProgramAId, ptActivity);

    System.out.println("   -> SUCCESS: Physio '" + physio.getUsername()
        + "' added PT activity '" + ptActivity.getName()
        + "' to Patient (ID: " + patientA.getId() + ")");

    System.out.println("\n[TEST] UC11: View Daily Physiotherapy Activities...");
    therapyProgressService.getPTActivities(ptProgramAId)
        .forEach(a -> System.out.println("   -> Activity: " + a.getName() + " | Completed: " + a.isCompleted()));
  }

  // --- 6) Occupational Therapy Module (OT-focused demo) ---
  private void demoOccupationalTherapy() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] OCCUPATIONAL THERAPY MODULE DEMO (OT)");
    System.out.println("==========================================");

    User physio = findAnyPhysioOrThrow();

    User patientA = seedPatient("patientA", "patientA@test.com");

    OTProgram otProgramA = therapyDataInitializer.createOTProgram(patientA.getId());
    Long otProgramAId = otProgramA.getId();

    System.out.println("\n[TEST] UC21: Modify Patient’s Occupational Therapy Activities...");
    OTActivity otActivity = new OTActivity();
    otActivity.setName("Grip Practice");
    otActivity.setDescription("Practice hand grip using soft ball");

    therapyManagementService.addOTActivity(otProgramAId, otActivity);

    System.out.println("   -> SUCCESS: Physio '" + physio.getUsername()
        + "' added OT activity '" + otActivity.getName()
        + "' to Patient (ID: " + patientA.getId() + ")");

    System.out.println("\n[TEST] UC12: View Daily Occupational Therapy Activities...");
    therapyProgressService.getOTActivities(otProgramAId)
        .forEach(a -> System.out.println("   -> Activity: " + a.getName() + " | Completed: " + a.isCompleted()));
  }

  // =========================
  // PLACEHOLDERS FOR MODULES NOT IMPLEMENTED YET
  // =========================
  private void demoAppointmentPlaceholder() {
    System.out.println("\n[INFO] Appointment Booking Module demo not wired yet.");
    System.out.println("       Add AppointmentService injection + demo method here.");
  }

  private void demoNotificationPlaceholder() {
    System.out.println("\n[INFO] Notification Pushing Module demo not wired yet.");
    System.out.println("       Add NotificationService injection + demo method here.");
  }

  private void demoFirstTimeScreeningPlaceholder() {
    System.out.println("\n[INFO] First Time Screening Module demo not wired yet.");
    System.out.println("       Add ScreeningService injection + demo method here.");
  }

  private void demoProgressTrackingPlaceholder() {
    System.out.println("\n[INFO] Patient Progress Tracking Module demo not wired yet.");
    System.out.println("       Add ProgressTrackingService injection + demo method here.");
  }

  private void demoJournalPlaceholder() {
    System.out.println("\n[INFO] Manage Journal Module demo not wired yet.");
    System.out.println("       Add JournalService injection + demo method here.");
  }

  private void demoSummaryPlaceholder() {
    System.out.println("\n[INFO] Summary Report Module demo not wired yet.");
    System.out.println("       Add SummaryService injection + demo method here.");
  }

  // =========================
  // HELPERS
  // =========================
  private User findAnyPhysioOrThrow() {
    return userRepository.findAll().stream()
        .filter(u -> "PHYSIO".equalsIgnoreCase(u.getRole()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Physiotherapist not found (seed a PHYSIO user first)."));
  }

  private User seedPatient(String username, String email) {
    return userRepository.findAll().stream()
        .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
        .findFirst()
        .orElseGet(() -> {
          User patient = new User();
          patient.setUsername(username);
          patient.setEmail(email);
          patient.setPassword("Password123");
          patient.setRole("PATIENT");
          patient.setActive(true);
          return userRepository.save(patient);
        });
  }
}
