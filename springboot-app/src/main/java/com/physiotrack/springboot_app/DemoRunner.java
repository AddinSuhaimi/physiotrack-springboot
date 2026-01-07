package com.physiotrack.springboot_app;

import java.util.List;

import com.physiotrack.usermanagement.model.User;
import com.physiotrack.usermanagement.service.UserManagementService;
import com.physiotrack.personalinfo.service.PersonalInfoService;
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
    PersonalInfoService personalInfoService
      // AppointmentService appointmentService,
      // AuthenticationService authenticationService,
      // NotificationService notificationService
  ) {
    this.userManagementService = userManagementService;
    this.personalInfoService = personalInfoService;
      // this.appointmentService = appointmentService;
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

        System.out.println("\n[TEST] UC05: User Changes Language Preference...");
        try {
            User langUser = personalInfoService.updateLanguage(1L, "ms"); // Change to Malay
            System.out.println("   -> SUCCESS: Language changed to: " + langUser.getLanguagePreference());
        } catch (Exception e) {
            System.out.println("   -> FAILED: " + e.getMessage());
        }

        System.out.println("\n[TEST] UC27: Admin Deactivates User...");
        try {
            User deactivatedUser = userManagementService.deactivateUser(1L);
            System.out.println("   -> SUCCESS: User status is now Active? " + deactivatedUser.isActive());
        } catch (Exception e) {
            System.out.println("   -> FAILED: " + e.getMessage());
        }

        System.out.println("\n==========================================");
        System.out.println("   DEMO COMPLETED");
        System.out.println("==========================================");

    // ========= MODULE DEMOS =========
    // Each module owner adds a method call here (keep it short + testable).
    //
    // demoAuthentication();
    // demoAppointment();
    // demoProgressTracking();
  }

  // ========= SAMPLE DEMO METHOD TEMPLATE =========
  // private void demoAppointment() {
  //   System.out.println("\n--- [APPOINTMENT MODULE DEMO] ---");
  //   appointmentService.createAppointment(...);
  //   appointmentService.approveAppointment(...);
  //   appointmentService.getSchedule(...);
  //   appointmentService.cancelAppointment(...);
  // }
}
