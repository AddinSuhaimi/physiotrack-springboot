package com.physiotrack.springboot_app;

import java.util.List;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;
import com.physiotrack.personalinfo.service.PersonalInfoService;
import com.physiotrack.usermanagement.repository.UserRepository;
import com.physiotrack.therapy.model.PTProgram;
import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.OTProgram;
import com.physiotrack.therapy.api.TherapyManagementService;
import com.physiotrack.therapy.api.TherapyProgressService;
import com.physiotrack.therapy.init.TherapyDataInitializer;
import com.physiotrack.therapy.model.OTActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * TEAM TEMPLATE: DemoRunner
 *
 * Purpose:
 * - This is the ONLY console entry-point used for Spring Boot grading.
 * - Each module owner should add a small "demo section" here to prove their 4 functionalities.
 *
 * Rule:
 * - Do NOT implement business logic in this class.
 * - Only call service interfaces from modules and print results.
 *
 * How teammates contribute:
 * - Each module owner adds:
 *   (1) a constructor-injected service interface
 *   (2) a demo method call inside run()
 *
 * Example (Appointment):
 *   private final AppointmentService appointmentService;
 *   DemoRunner(AppointmentService appointmentService) { this.appointmentService = appointmentService; }
 *   ...
 *   appointmentServiceDemo();
 */
@Component
public class DemoRunner implements CommandLineRunner {

  private final UserManagementService userManagementService;
  private final PersonalInfoService personalInfoService;
  private final TherapyManagementService therapyManagementService;
  private final TherapyProgressService therapyProgressService;
  private final UserRepository userRepository;
  private final TherapyDataInitializer therapyDataInitializer;


  // ========= MODULE SERVICE INTERFACES (inject here) =========
  // Add interfaces from each module (service interfaces live in that module)
  //
  // Example:
  // private final AppointmentService appointmentService;
  // private final AuthenticationService authenticationService;
  // private final NotificationService notificationService;

  // ========= CONSTRUCTOR INJECTION =========
  // Add parameters for each service interface you inject.
  @Autowired
  public DemoRunner(
    UserManagementService userManagementService,
    PersonalInfoService personalInfoService,
    TherapyManagementService therapyManagementService,
    TherapyProgressService therapyProgressService,
    UserRepository userRepository,
    TherapyDataInitializer therapyDataInitializer
      // AuthenticationService authenticationService,
      // NotificationService notificationService
  ) {
    this.userManagementService = userManagementService;
    this.personalInfoService = personalInfoService;
    this.therapyManagementService = therapyManagementService;
    this.therapyProgressService = therapyProgressService;
    this.userRepository = userRepository;
    this.therapyDataInitializer = therapyDataInitializer;
      // this.authenticationService = authenticationService;
      // this.notificationService = notificationService;
  }

  @Override
  public void run(String... args) {
    System.out.println("=======================================");
    System.out.println("[DEMO] PhysioTrack Spring Boot started");
    System.out.println("=======================================");

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

        System.out.println("\n[TEST] UC04: User Updates Profile...");
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

  // --- 4) Physiotherapy Module (PT-focused demo) ---
  private void demoPhysiotherapy() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] THERAPY MODULE DEMO");
    System.out.println("==========================================");

    Scanner scanner = new Scanner(System.in);

    // User selection menu
    System.out.println("\n--- Select User Role ---");
    System.out.println("1) Login as Physiotherapist");
    System.out.println("2) Login as Patient");
    System.out.println("0) Back to Main Menu");

    int roleChoice = readInt(scanner, "Select role: ");

    if (roleChoice == 0) {
        return;
    }

    User selectedUser;
    if (roleChoice == 1) {
        // Login as Physio
        selectedUser = selectUser(scanner, "PHYSIO");
        if (selectedUser == null) return;

        System.out.println("\n[LOGGED IN] Physiotherapist: " + selectedUser.getUsername() + " (ID: " + selectedUser.getId() + ")");

        // Physio can select a patient
        User patient = selectUser(scanner, "PATIENT");
        if (patient == null) return;

        System.out.println("[SELECTED] Patient: " + patient.getUsername() + " (ID: " + patient.getId() + ")");

        // Get or create PT program for patient (fetch existing if any)
        PTProgram ptProgram = getOrCreatePTProgram(patient.getId());
        Long ptProgramId = ptProgram.getId();

        // UC11: View Patient's Physiotherapy Activities FIRST
        System.out.println("\nView Patient's Physiotherapy Activities...");
        List<PTActivity> activities = therapyProgressService.getPTActivities(ptProgramId);

        if (activities.isEmpty()) {
            System.out.println("   -> No activities found for this patient.");
        } else {
            System.out.println("\n--- Current Activities ---");
            for (int i = 0; i < activities.size(); i++) {
                PTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName()
                        + " | Completed: " + a.isCompleted());
            }
        }

        // UC20: Modify options
        System.out.println("\n--- UC20: Modify Patient's Physiotherapy Activities ---");
        System.out.println(" " + (activities.size() + 1) + ") Add New Activity");
        if (!activities.isEmpty()) {
            System.out.println(" " + (activities.size() + 2) + ") Remove Activity");
        }
        System.out.println(" 0) Back to Main Menu");

        int actionChoice = readInt(scanner, "\nSelect option: ");

        if (actionChoice == 0) {
            return;
        }

        if (actionChoice == activities.size() + 1) {
            // Add new activity
            System.out.println("\n[ADD] Adding New Activity...");

            String activityName = readString(scanner, "Enter activity name: ");

            PTActivity ptActivity = new PTActivity();
            ptActivity.setName(activityName);
            ptActivity.setCompleted(false);

            try {
                therapyManagementService.addPTActivity(ptProgramId, ptActivity);

                System.out.println("   -> SUCCESS: Physio '" + selectedUser.getUsername()
                        + "' added PT activity '" + ptActivity.getName()
                        + "' to Patient '" + patient.getUsername() + "'");

                // Show updated list
                System.out.println("\n--- Updated Activities List ---");
                List<PTActivity> updatedActivities = therapyProgressService.getPTActivities(ptProgramId);
                for (int i = 0; i < updatedActivities.size(); i++) {
                    PTActivity a = updatedActivities.get(i);
                    System.out.println(" " + (i + 1) + ") " + a.getName()
                            + " | Completed: " + a.isCompleted());
                }
            } catch (Exception e) {
                System.out.println("   -> FAILED: " + e.getMessage());
                e.printStackTrace();
            }

        } else if (!activities.isEmpty() && actionChoice == activities.size() + 2) {
            // Remove activity
            System.out.println("\n[REMOVE] Select Activity to Remove:");
            for (int i = 0; i < activities.size(); i++) {
                PTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName());
            }
            System.out.println(" 0) Cancel");

            int removeChoice = readInt(scanner, "\nSelect activity to remove (1-" + activities.size() + ", 0 to cancel): ");

            if (removeChoice == 0) {
                System.out.println("[INFO] Removal cancelled.");
                return;
            }

            if (removeChoice >= 1 && removeChoice <= activities.size()) {
                PTActivity activityToRemove = activities.get(removeChoice - 1);

                try {
                    therapyManagementService.removePTActivity(ptProgramId, activityToRemove.getId());

                    System.out.println("   -> SUCCESS: Activity '" + activityToRemove.getName() + "' removed.");

                    // Show updated list
                    System.out.println("\n--- Updated Activities List ---");
                    List<PTActivity> updatedActivities = therapyProgressService.getPTActivities(ptProgramId);
                    if (updatedActivities.isEmpty()) {
                        System.out.println("   -> No activities remaining.");
                    } else {
                        for (int i = 0; i < updatedActivities.size(); i++) {
                            PTActivity a = updatedActivities.get(i);
                            System.out.println(" " + (i + 1) + ") " + a.getName()
                                    + " | Completed: " + a.isCompleted());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("   -> FAILED: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("   -> ERROR: Invalid selection.");
            }

        } else {
            System.out.println("[ERROR] Invalid selection.");
        }

    } else if (roleChoice == 2) {
        selectedUser = selectUser(scanner, "PATIENT");
        if (selectedUser == null) return;

        System.out.println("\n[LOGGED IN] Patient: " + selectedUser.getUsername() + " (ID: " + selectedUser.getId() + ")");

        // Get or create PT program for this patient
        PTProgram ptProgram = getOrCreatePTProgram(selectedUser.getId());
        Long ptProgramId = ptProgram.getId();

        // UC11: View Daily Physiotherapy Activities
        System.out.println("\nView Daily Physiotherapy Activities...");
        List<PTActivity> activities = therapyProgressService.getPTActivities(ptProgramId);

        if (activities.isEmpty()) {
            System.out.println("   -> No activities assigned yet.");
            System.out.println("   -> Please ask your physiotherapist to assign activities.");
        } else {
            System.out.println("\n--- Your Activities ---");
            for (int i = 0; i < activities.size(); i++) {
                PTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName()
                        + " | Completed: " + a.isCompleted());
            }

            // UC13: Mark activity as complete (only if there are activities)
            System.out.println("\nWould you like to mark an activity as completed?");
            String response = readString(scanner, "Response (yes/no): ");

            if ("yes".equalsIgnoreCase(response)) {
                System.out.println("\n[UC13] Mark Activity as Completed");

                int activityChoice = readInt(scanner, "Select activity to mark as completed (1-" + activities.size() + ", 0 to cancel): ");

                if (activityChoice == 0) {
                    System.out.println("[INFO] Action cancelled.");
                    return;
                }

                if (activityChoice >= 1 && activityChoice <= activities.size()) {
                    PTActivity selectedActivity = activities.get(activityChoice - 1);

                    if (selectedActivity.isCompleted()) {
                        System.out.println("   -> INFO: Activity '" + selectedActivity.getName() + "' is already marked as completed.");
                    } else {
                        try {
                            therapyProgressService.markPTCompleted(ptProgramId, selectedActivity.getId());
                            System.out.println("   -> SUCCESS: Activity '" + selectedActivity.getName() + "' marked as completed!");

                            // Show updated list
                            System.out.println("\n--- Updated Activities List ---");
                            List<PTActivity> updatedActivities = therapyProgressService.getPTActivities(ptProgramId);
                            for (int i = 0; i < updatedActivities.size(); i++) {
                                PTActivity a = updatedActivities.get(i);
                                System.out.println(" " + (i + 1) + ") " + a.getName()
                                        + " | Completed: " + a.isCompleted());
                            }
                        } catch (Exception e) {
                            System.out.println("   -> FAILED: " + e.getMessage());
                        }
                    }
                }
            }
        }
    } else {
        System.out.println("[ERROR] Invalid role selection.");
    }
}

  // --- 5) Occupational Therapy Module (OT-focused demo) ---
  private void demoOccupationalTherapy() {
    System.out.println("\n==========================================");
    System.out.println("[TEST] OCCUPATIONAL THERAPY MODULE DEMO (OT)");
    System.out.println("==========================================");

    Scanner scanner = new Scanner(System.in);

    // User selection menu
    System.out.println("\n--- Select User Role ---");
    System.out.println("1) Login as Physiotherapist");
    System.out.println("2) Login as Patient");
    System.out.println("0) Back to Main Menu");

    int roleChoice = readInt(scanner, "Select role: ");

    if (roleChoice == 0) {
        return;
    }

    User selectedUser;
    if (roleChoice == 1) {
        // Login as Physio
        selectedUser = selectUser(scanner, "PHYSIO");
        if (selectedUser == null) return;

        System.out.println("\n[LOGGED IN] Physiotherapist: " + selectedUser.getUsername() + " (ID: " + selectedUser.getId() + ")");

        // Physio can select a patient
        User patient = selectUser(scanner, "PATIENT");
        if (patient == null) return;

        System.out.println("[SELECTED] Patient: " + patient.getUsername() + " (ID: " + patient.getId() + ")");

        // Get or create OT program for patient (fetch existing if any)
        OTProgram otProgram = getOrCreateOTProgram(patient.getId());
        Long otProgramId = otProgram.getId();

        // UC12: View Patient's Occupational Therapy Activities FIRST
        System.out.println("\nView Patient's Occupational Therapy Activities...");
        List<OTActivity> activities = therapyProgressService.getOTActivities(otProgramId);

        if (activities.isEmpty()) {
            System.out.println("   -> No activities found for this patient.");
        } else {
            System.out.println("\n--- Current Activities ---");
            for (int i = 0; i < activities.size(); i++) {
                OTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName()
                        + " | Completed: " + a.isCompleted());
            }
        }

        // UC21: Modify options
        System.out.println("\n--- UC21: Modify Patient's Occupational Therapy Activities ---");
        System.out.println(" " + (activities.size() + 1) + ") Add New Activity");
        if (!activities.isEmpty()) {
            System.out.println(" " + (activities.size() + 2) + ") Remove Activity");
        }
        System.out.println(" 0) Back to Main Menu");

        int actionChoice = readInt(scanner, "\nSelect option: ");

        if (actionChoice == 0) {
            return;
        }

        if (actionChoice == activities.size() + 1) {
            // Add new activity
            System.out.println("\n[ADD] Adding New Activity...");

            String activityName = readString(scanner, "Enter activity name: ");

            OTActivity otActivity = new OTActivity();
            otActivity.setName(activityName);
            otActivity.setCompleted(false);

            try {
                therapyManagementService.addOTActivity(otProgramId, otActivity);

                System.out.println("   -> SUCCESS: Physio '" + selectedUser.getUsername()
                        + "' added OT activity '" + otActivity.getName()
                        + "' to Patient '" + patient.getUsername() + "'");

                // Show updated list
                System.out.println("\n--- Updated Activities List ---");
                List<OTActivity> updatedActivities = therapyProgressService.getOTActivities(otProgramId);
                for (int i = 0; i < updatedActivities.size(); i++) {
                    OTActivity a = updatedActivities.get(i);
                    System.out.println(" " + (i + 1) + ") " + a.getName()
                            + " | Completed: " + a.isCompleted());
                }
            } catch (Exception e) {
                System.out.println("   -> FAILED: " + e.getMessage());
                e.printStackTrace();
            }

        } else if (!activities.isEmpty() && actionChoice == activities.size() + 2) {
            // Remove activity
            System.out.println("\n[REMOVE] Select Activity to Remove:");
            for (int i = 0; i < activities.size(); i++) {
                OTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName());
            }
            System.out.println(" 0) Cancel");

            int removeChoice = readInt(scanner, "\nSelect activity to remove (1-" + activities.size() + ", 0 to cancel): ");

            if (removeChoice == 0) {
                System.out.println("[INFO] Removal cancelled.");
                return;
            }

            if (removeChoice >= 1 && removeChoice <= activities.size()) {
                OTActivity activityToRemove = activities.get(removeChoice - 1);

                try {
                    therapyManagementService.removeOTActivity(otProgramId, activityToRemove.getId());

                    System.out.println("   -> SUCCESS: Activity '" + activityToRemove.getName() + "' removed.");

                    // Show updated list
                    System.out.println("\n--- Updated Activities List ---");
                    List<OTActivity> updatedActivities = therapyProgressService.getOTActivities(otProgramId);
                    if (updatedActivities.isEmpty()) {
                        System.out.println("   -> No activities remaining.");
                    } else {
                        for (int i = 0; i < updatedActivities.size(); i++) {
                            OTActivity a = updatedActivities.get(i);
                            System.out.println(" " + (i + 1) + ") " + a.getName()
                                    + " | Completed: " + a.isCompleted());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("   -> FAILED: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("   -> ERROR: Invalid selection.");
            }

        } else {
            System.out.println("[ERROR] Invalid selection.");
        }

    } else if (roleChoice == 2) {
        // Login as Patient
        selectedUser = selectUser(scanner, "PATIENT");
        if (selectedUser == null) return;

        System.out.println("\n[LOGGED IN] Patient: " + selectedUser.getUsername() + " (ID: " + selectedUser.getId() + ")");

        // Get or create OT program for this patient
        OTProgram otProgram = getOrCreateOTProgram(selectedUser.getId());
        Long otProgramId = otProgram.getId();

        // UC12: View Daily Occupational Therapy Activities
        System.out.println("\nView Daily Occupational Therapy Activities...");
        List<OTActivity> activities = therapyProgressService.getOTActivities(otProgramId);

        if (activities.isEmpty()) {
            System.out.println("   -> No activities assigned yet.");
            System.out.println("   -> Please ask your physiotherapist to assign activities.");
        } else {
            System.out.println("\n--- Your Activities ---");
            for (int i = 0; i < activities.size(); i++) {
                OTActivity a = activities.get(i);
                System.out.println(" " + (i + 1) + ") " + a.getName()
                        + " | Completed: " + a.isCompleted());
            }

            // Mark activity as complete (only if there are activities)
            System.out.println("\nWould you like to mark an activity as completed?");
            String response = readString(scanner, "Response (yes/no): ");

            if ("yes".equalsIgnoreCase(response)) {
                int activityChoice = readInt(scanner, "Select activity to mark as completed (1-" + activities.size() + ", 0 to cancel): ");

                if (activityChoice == 0) {
                    System.out.println("[INFO] Action cancelled.");
                    return;
                }

                if (activityChoice >= 1 && activityChoice <= activities.size()) {
                    OTActivity selectedActivity = activities.get(activityChoice - 1);

                    if (selectedActivity.isCompleted()) {
                        System.out.println("   -> INFO: Activity '" + selectedActivity.getName() + "' is already marked as completed.");
                    } else {
                        try {
                            therapyProgressService.markOTCompleted(otProgramId, selectedActivity.getId());
                            System.out.println("   -> SUCCESS: Activity '" + selectedActivity.getName() + "' marked as completed!");

                            // Show updated list
                            System.out.println("\n--- Updated Activities List ---");
                            List<OTActivity> updatedActivities = therapyProgressService.getOTActivities(otProgramId);
                            for (int i = 0; i < updatedActivities.size(); i++) {
                                OTActivity a = updatedActivities.get(i);
                                System.out.println(" " + (i + 1) + ") " + a.getName()
                                        + " | Completed: " + a.isCompleted());
                            }
                        } catch (Exception e) {
                            System.out.println("   -> FAILED: " + e.getMessage());
                        }
                    }
                }
            }
        }
    } else {
        System.out.println("[ERROR] Invalid role selection.");
    }
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
    System.out.println("\n[UC15] Create booking request");

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
    System.out.println("\n[UC16] Create update request");

    Appointment target = chooseConfirmedAppointmentForPatient(scanner, patientId);
    if (target == null) return;

    Long physioId = target.getPhysioId();
    System.out.println("[UC16] Target appointment: id=" + target.getId()
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
    System.out.println("\n[UC17] Create cancel request");

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
    System.out.println("\n[UC24] Manage appointment requests");

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
    System.out.println("\n[UC18] View physio schedule");

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
      System.out.println("       Create booking request (UC15) then approve it (UC24) first.");
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

  private User selectUser(Scanner scanner, String role) {
    List<User> users = userRepository.findAll().stream()
        .filter(u -> role.equalsIgnoreCase(u.getRole()) && u.isActive())
        .toList();
    
    if (users.isEmpty()) {
      System.out.println("[ERROR] No active " + role + " users found in the system.");
      System.out.println("        Please register users first in User Management Module.");
      return null;
    }
    
    System.out.println("\nAvailable " + role + " Users:");
    for (int i = 0; i < users.size(); i++) {
      User u = users.get(i);
      System.out.println(" " + (i + 1) + ") " + u.getUsername() 
          + " (ID: " + u.getId() 
          + ", Email: " + u.getEmail() + ")");
    }
    System.out.println(" 0) Cancel");
    
    while (true) {
      int choice = readInt(scanner, "Select user (0 to cancel): ");
      
      if (choice == 0) {
        System.out.println("[INFO] Selection cancelled.");
        return null;
      }
      
      if (choice >= 1 && choice <= users.size()) {
        return users.get(choice - 1);
      }
      
      System.out.println("[ERROR] Invalid selection. Please choose 0-" + users.size());
    }
  }

  private PTProgram getOrCreatePTProgram(Long patientId) {
    PTProgram program = therapyManagementService.findPTProgramByPatientId(patientId);
    if (program != null) {
        return program;
    }
    return therapyDataInitializer.createPTProgram(patientId);
}
  
  private OTProgram getOrCreateOTProgram(Long patientId) {
    OTProgram program = therapyManagementService.findOTProgramByPatientId(patientId);
    if (program != null) {
        return program;
    }
    return therapyDataInitializer.createOTProgram(patientId);
  }
}
