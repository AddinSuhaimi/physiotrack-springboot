package com.physiotrack.springboot_app;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
import com.physiotrack.appointment.model.Appointment;
import com.physiotrack.appointment.service.AppointmentService;
import com.physiotrack.appointment.service.ScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

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
@Order(2)
@Component
public class DemoRunner implements CommandLineRunner {

  // ADD HERE AFTER DONE
  private final UserManagementService userManagementService;
  private final PersonalInfoService personalInfoService;

  private final TherapyManagementService therapyManagementService;
  private final TherapyProgressService therapyProgressService;
  private final TherapyDataInitializer therapyDataInitializer;

  private final AppointmentService appointmentService;
  private final ScheduleService scheduleService;

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
      TherapyDataInitializer therapyDataInitializer,
      AppointmentService appointmentService,
      ScheduleService scheduleService
  ) {
    this.userManagementService = userManagementService;
    this.personalInfoService = personalInfoService;
    this.therapyManagementService = therapyManagementService;
    this.therapyProgressService = therapyProgressService;
    this.userRepository = userRepository;
    this.therapyDataInitializer = therapyDataInitializer;
    this.appointmentService = appointmentService;
    this.scheduleService = scheduleService;
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
          appointmentMenu(scanner);
          break;

        case 4:
          demoPhysiotherapy();
          break;

        case 5:
          demoOccupationalTherapy();
          break;

        case 6:
          demoFirstTimeScreeningPlaceholder();
          break;

        case 7:
          demoProgressTrackingPlaceholder();
          break;

        case 8:
          demoJournalPlaceholder();
          break;

        case 9:
          demoSummaryPlaceholder();
          break;

        default:
          System.out.println("[ERROR] Invalid selection. Please choose 0-9.");
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
    System.out.println(" 4) Physiotherapy Module");
    System.out.println(" 5) Occupational Therapy Module");
    System.out.println(" 6) First Time Screening Module");
    System.out.println(" 7) Patient Progress Tracking Module");
    System.out.println(" 8) Manage Journal Module");
    System.out.println(" 9) Summary Report Module");
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

  // -------------------- Appointment Booking Menu --------------------

  private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private void appointmentMenu(Scanner scanner) {
    System.out.println("\n==========================================");
    System.out.println("APPOINTMENT BOOKING MODULE DEMO");
    System.out.println("==========================================");

    User patient = findAnyPatientOrThrow();
    User physio  = findAnyPhysioOrThrow();

    Long patientId = patient.getId();
    Long physioId  = physio.getId();

    System.out.println("[APPOINTMENT] Using seeded patient: " + patient.getUsername()
        + " (id=" + patientId + ", email=" + patient.getEmail() + ")");
    System.out.println("[APPOINTMENT] Using seeded physio : " + physio.getUsername()
        + " (id=" + physioId + ", email=" + physio.getEmail() + ")");


    boolean loop = true;
    while (loop) {
      System.out.println("\n---------- Appointment Menu ----------");
      System.out.println("1) Create booking request");
      System.out.println("2) Create update request");
      System.out.println("3) Create cancel request");
      System.out.println("4) Manage appointment requests (admin)");
      System.out.println("5) View physio schedule (range)");
      System.out.println("6) Check slot availability");
      System.out.println("0) Back");
      System.out.println("--------------------------------------");

      int c = readInt(scanner, "Select: ");

      try {
        switch (c) {
          case 0 -> loop = false;

          case 1 -> uc15_createBooking(scanner, patientId, physioId);

          case 2 -> uc16_createUpdate(scanner, patientId);

          case 3 -> uc17_createCancel(scanner, patientId);

          case 4 -> uc24_manageAppointmentRequests(scanner);

          case 5 -> uc18_viewSchedule(scanner, physioId);

          case 6 -> uc18_checkSlot(scanner, physioId);

          default -> System.out.println("[ERROR] Invalid selection.");
        }
      } catch (Exception e) {
        System.out.println("[FAILED] " + e.getMessage());
      }
    }
  }

  // UC15 - Create booking request
  private void uc15_createBooking(Scanner scanner, Long patientId, Long ignoredPhysioId) {
    System.out.println("\nCreate booking request");

    User physio = choosePhysio(scanner);
    Long physioId = physio.getId();

    LocalDateTime dt = chooseSlot(scanner, physioId);
    if (dt == null) {
      System.out.println("Please choose a timeslot.");
      return;
    }

    String details = readString(scanner, "Enter appointment details: ");

    Appointment a = appointmentService.createBookingRequest(patientId, physioId, dt, details);

    System.out.println("The request of appointment booking is sent to admin successfully.");
    System.out.println("[OK] Booking request created (PENDING).");
    printAppointment(a);
  }

  // UC16 - Create update request
  private void uc16_createUpdate(Scanner scanner, Long patientId) {
    System.out.println("\nCreate update request");

    Appointment target = chooseConfirmedAppointmentForPatient(scanner, patientId);
    if (target == null) return;

    Long physioId = target.getPhysioId();
    System.out.println("Target appointment: id=" + target.getId()
        + " | physioId=" + physioId
        + " | currentDateTime=" + target.getDateTime());

    while (true) {
      LocalDateTime newDt = chooseSlot(scanner, physioId);
      if (newDt == null) {
        System.out.println("Please choose a timeslot.");
        return;
      }

      String newDetails = readString(scanner, "Enter new details: ");

      try {
        Appointment req = appointmentService.createUpdateRequest(patientId, target.getId(), newDt, newDetails);

        System.out.println("The request of editing appointment details is sent to admin successfully.");
        System.out.println("[OK] Update request created (PENDING).");
        printAppointment(req);
        return;

      } catch (IllegalArgumentException e) {
        // Specifically handle slot conflicts and timeslot-required messages
        System.out.println("[FAILED] " + e.getMessage());

        String again = readString(scanner, "Choose another timeslot? (yes/no): ");
        if (!"yes".equalsIgnoreCase(again)) {
          System.out.println("[INFO] Update aborted.");
          return;
        }
        // loop continues, showing slots again
      }
    }
  }

  // UC17 - Create cancel request
  private void uc17_createCancel(Scanner scanner, Long patientId) {
    System.out.println("\nCreate cancel request");

    Appointment target = chooseConfirmedAppointmentForPatient(scanner, patientId);
    if (target == null) return;

    System.out.println("Are you sure to cancel the appointment?");
    String confirm = readString(scanner, "Confirm cancel? (yes/no): ");

    if (!"yes".equalsIgnoreCase(confirm)) {
      System.out.println("[INFO] Cancel aborted.");
      return;
    }

    Appointment req = appointmentService.createCancelRequest(patientId, target.getId());

    System.out.println("[OK] Cancel request created (PENDING).");
    printAppointment(req);
  }

  // UC24 - Manage appointment requests
  private void uc24_manageAppointmentRequests(Scanner scanner) {
    System.out.println("\nManage appointment requests");

    while (true) {
      List<Appointment> newReqs    = appointmentService.listPendingNewRequests();
      List<Appointment> updateReqs = appointmentService.listPendingUpdateRequests();
      List<Appointment> cancelReqs = appointmentService.listPendingCancelRequests();

      if (newReqs.isEmpty() && updateReqs.isEmpty() && cancelReqs.isEmpty()) {
        System.out.println("[INFO] No pending requests (NEW/UPDATE/CANCEL).");
        return;
      }

      java.util.List<Appointment> flat = new java.util.ArrayList<>();

      System.out.println("\n--- PENDING REQUESTS ---");

      System.out.println("\n[NEW] (" + newReqs.size() + ")");
      for (Appointment a : newReqs) {
        flat.add(a);
        System.out.print(" " + flat.size() + ") ");
        printAppointment(a);
      }

      System.out.println("\n[UPDATE] (" + updateReqs.size() + ")");
      for (Appointment a : updateReqs) {
        flat.add(a);
        System.out.print(" " + flat.size() + ") ");
        printAppointment(a);
      }

      System.out.println("\n[CANCEL] (" + cancelReqs.size() + ")");
      for (Appointment a : cancelReqs) {
        flat.add(a);
        System.out.print(" " + flat.size() + ") ");
        printAppointment(a);
      }

      System.out.println("\nActions:");
      System.out.println("1) Approve");
      System.out.println("2) Reject");
      System.out.println("0) Back");

      int action = readInt(scanner, "Select action: ");
      if (action == 0) return;

      int idx = readInt(scanner, "Choose request number (1-" + flat.size() + "): ");
      if (idx < 1 || idx > flat.size()) {
        System.out.println("[ERROR] Invalid selection.");
        continue;
      }

      Long requestId = flat.get(idx - 1).getId();

      try {
        Appointment result = switch (action) {
          case 1 -> appointmentService.approveRequest(requestId);
          case 2 -> appointmentService.rejectRequest(requestId);
          default -> {
            System.out.println("[ERROR] Invalid action.");
            yield null;
          }
        };

        if (result != null) {
          System.out.println(action == 1 ? "[OK] Request approved" : "[OK] Request rejected");
          printAppointment(result);
          System.out.println("[INFO] Notification simulated (see [NOTIFY] output above).");
        }

      } catch (Exception e) {
        System.out.println("[FAILED] " + e.getMessage());
      }
    }
  }

  // -------------------- UC18 schedule --------------------
  private void uc18_viewSchedule(Scanner scanner, Long physioId) {
    System.out.println("\nView physio schedule");

    LocalDateTime from = readDateTime(scanner, "From (yyyy-MM-dd HH:mm): ");
    LocalDateTime to   = readDateTime(scanner, "To   (yyyy-MM-dd HH:mm): ");

    List<Appointment> appts = scheduleService.getScheduleForPhysio(physioId, from, to);

    if (appts.isEmpty()) {
      System.out.println("No appointments scheduled in the range.");
      return;
    }

    System.out.println("Appointments (" + appts.size() + "):");
    appts.forEach(this::printAppointment);
  }

  // -------------------- slot check --------------------
  private void uc18_checkSlot(Scanner scanner, Long physioId) {
    System.out.println("\n[CHECK] Slot availability");
    LocalDateTime dt = readDateTime(scanner, "Enter date+time (yyyy-MM-dd HH:mm): ");
    boolean ok = scheduleService.isSlotAvailable(physioId, dt);
    System.out.println(ok ? "[OK] Slot is available" : "[NOT AVAILABLE] Slot is already taken");
  }

  // =========================
  // PLACEHOLDERS FOR MODULES NOT IMPLEMENTED YET
  // =========================

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

  private List<User> listPhysiosOrThrow() {
    List<User> physios = userRepository.findAll().stream()
        .filter(u -> "PHYSIO".equalsIgnoreCase(u.getRole()))
        .toList();

    if (physios.isEmpty()) {
      throw new RuntimeException("No PHYSIO users found (seed a PHYSIO user first).");
    }
    return physios;
  }

  private User choosePhysio(Scanner scanner) {
    List<User> physios = listPhysiosOrThrow();

    System.out.println("\nAvailable Physiotherapists:");
    for (int i = 0; i < physios.size(); i++) {
      User p = physios.get(i);
      System.out.println(" " + (i + 1) + ") " + p.getUsername()
          + " (id=" + p.getId()
          + (p.getClinicName() != null ? ", clinic=" + p.getClinicName() : "")
          + ")");
    }

    while (true) {
      int idx = readInt(scanner, "Choose physiotherapist (1-" + physios.size() + "): ");
      if (idx >= 1 && idx <= physios.size()) return physios.get(idx - 1);
      System.out.println("[ERROR] Invalid selection.");
    }
  }

  private User findAnyPatientOrThrow() {
    return userRepository.findAll().stream()
        .filter(u -> "PATIENT".equalsIgnoreCase(u.getRole()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Patient not found (seed a PATIENT user first)."));
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

  private void printAppointment(Appointment a) {
    if (a == null) {
      System.out.println("  - null");
      return;
    }

    System.out.println("  - requestId=" + a.getId()
        + " | type=" + a.getRequestType()
        + " | status=" + a.getStatus()
        + " | patientId=" + a.getPatientId()
        + " | physioId=" + a.getPhysioId()
        + " | dateTime=" + a.getDateTime()
        + (a.getTargetAppointmentId() != null ? " | targetAppointmentId=" + a.getTargetAppointmentId() : "")
        + (a.getDetails() != null ? " | details=" + a.getDetails() : "")
    );
  }

  private LocalDateTime readDateTime(Scanner scanner, String prompt) {
    while (true) {
      String s = readString(scanner, prompt);
      try {
        return LocalDateTime.parse(s, DT_FMT);
      } catch (DateTimeParseException e) {
        System.out.println("[ERROR] Invalid format. Use: yyyy-MM-dd HH:mm (example: 2026-01-20 10:00)");
      }
    }
  }

  private String readString(Scanner scanner, String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  private Long readLong(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt);
      String s = scanner.nextLine().trim();
      try {
        return Long.parseLong(s);
      } catch (NumberFormatException e) {
        System.out.println("[ERROR] Please enter a valid number.");
      }
    }
  }

  private Appointment chooseConfirmedAppointmentForPatient(Scanner scanner, Long patientId) {
    List<Appointment> confirmed = appointmentService.listConfirmedAppointmentsForPatient(patientId);

    if (confirmed.isEmpty()) {
      System.out.println("[INFO] No confirmed appointments found for this patient.");
      System.out.println("       Create booking request then approve it first.");
      return null;
    }

    System.out.println("\nConfirmed Appointments:");
    for (int i = 0; i < confirmed.size(); i++) {
      Appointment a = confirmed.get(i);
      System.out.println(" " + (i + 1) + ") id=" + a.getId()
          + " | physioId=" + a.getPhysioId()
          + " | dateTime=" + a.getDateTime()
          + " | details=" + (a.getDetails() == null ? "" : a.getDetails()));
    }

    int idx;
    while (true) {
      idx = readInt(scanner, "Choose appointment (1-" + confirmed.size() + "): ");
      if (idx >= 1 && idx <= confirmed.size()) break;
      System.out.println("[ERROR] Invalid selection.");
    }

    return confirmed.get(idx - 1);
  }

  private List<LocalDateTime> suggestAvailableSlots(Long physioId, int daysAhead) {
    LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
    List<LocalDateTime> slots = new java.util.ArrayList<>();

    for (int d = 0; d <= daysAhead; d++) {
      LocalDateTime day = now.plusDays(d);
      for (int hour = 9; hour <= 16; hour++) { // 09:00..16:00
        LocalDateTime candidate = day.withHour(hour).withMinute(0);
        if (candidate.isAfter(now) && scheduleService.isSlotAvailable(physioId, candidate)) {
          slots.add(candidate);
        }
      }
    }
    return slots;
  }

  private LocalDateTime chooseSlot(Scanner scanner, Long physioId) {
    List<LocalDateTime> slots = suggestAvailableSlots(physioId, 7);

    System.out.println("\nAvailable Slots (next 7 days):");
    if (slots.isEmpty()) {
      System.out.println("  (none found)");
      System.out.println("  0) Enter manually");
    } else {
      int show = Math.min(slots.size(), 15);
      for (int i = 0; i < show; i++) {
        System.out.println(" " + (i + 1) + ") " + slots.get(i).format(DT_FMT));
      }
      System.out.println("  0) Enter manually");
    }

    int choice = readInt(scanner, "Choose slot: ");
    if (choice == 0) {
      return readDateTime(scanner, "Enter date+time (yyyy-MM-dd HH:mm): ");
    }

    int max = Math.min(slots.size(), 15);
    if (choice < 1 || choice > max) {
      System.out.println("[ERROR] Invalid slot selection.");
      return null;
    }

    return slots.get(choice - 1);
  }

}
