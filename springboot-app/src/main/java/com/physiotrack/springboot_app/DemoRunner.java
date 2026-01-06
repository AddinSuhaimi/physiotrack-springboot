package com.physiotrack.springbootapp;

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

  // ========= MODULE SERVICE INTERFACES (inject here) =========
  // Add interfaces from each module (service interfaces live in that module)
  //
  // Example:
  // private final AppointmentService appointmentService;
  // private final AuthenticationService authenticationService;
  // private final NotificationService notificationService;

  // ========= CONSTRUCTOR INJECTION =========
  // Add parameters for each service interface you inject.
  public DemoRunner(
      // AppointmentService appointmentService,
      // AuthenticationService authenticationService,
      // NotificationService notificationService
  ) {
      // this.appointmentService = appointmentService;
      // this.authenticationService = authenticationService;
      // this.notificationService = notificationService;
  }

  @Override
  public void run(String... args) {
    System.out.println("=======================================");
    System.out.println("[DEMO] PhysioTrack Spring Boot started");
    System.out.println("=======================================");

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
