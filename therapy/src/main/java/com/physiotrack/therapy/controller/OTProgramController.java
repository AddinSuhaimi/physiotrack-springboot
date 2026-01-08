package com.physiotrack.therapy.controller;

import com.physiotrack.therapy.model.OTActivity;
import com.physiotrack.therapy.service.TherapyProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapy/ot")
public class OTProgramController {

    private final TherapyProgressService progressService;

    public OTProgramController(TherapyProgressService progressService) {
        this.progressService = progressService;
    }

    // UC12: View daily occupational therapy activities
    @GetMapping("/{programId}")
    public List<OTActivity> viewOTActivities(@PathVariable Long programId) {
        return progressService.getOTActivities(programId);
    }

    // Mark activity as completed
    @PutMapping("/{programId}/activity/{activityId}/complete")
    public void markCompleted(
            @PathVariable Long programId,
            @PathVariable Long activityId) {
        progressService.markOTCompleted(programId, activityId);
    }
}
