package com.physiotrack.therapy.controller;

import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.service.TherapyProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/therapy/pt")
public class PTProgramController {

    private final TherapyProgressService progressService;

    public PTProgramController(TherapyProgressService progressService) {
        this.progressService = progressService;
    }

    // UC11: View daily physiotherapy activities
    @GetMapping("/{programId}")
    public List<PTActivity> viewPTActivities(@PathVariable Long programId) {
        return progressService.getPTActivities(programId);
    }

    // Mark activity as completed
    @PutMapping("/{programId}/activity/{activityId}/complete")
    public void markCompleted(
            @PathVariable Long programId,
            @PathVariable Long activityId) {
        progressService.markPTCompleted(programId, activityId);
    }
}
