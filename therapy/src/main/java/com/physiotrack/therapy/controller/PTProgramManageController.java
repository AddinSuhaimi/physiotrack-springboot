package com.physiotrack.therapy.controller;

import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.service.TherapyManagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/therapy/manage/pt")
public class PTProgramManageController {

    private final TherapyManagementService managementService;

    public PTProgramManageController(TherapyManagementService managementService) {
        this.managementService = managementService;
    }

    @PostMapping("/{programId}/activity")
    public void addActivity(
            @PathVariable Long programId,
            @RequestBody PTActivity activity) {
        managementService.addPTActivity(programId, activity);
    }

    @DeleteMapping("/{programId}/activity/{activityId}")
    public void deleteActivity(
            @PathVariable Long programId,
            @PathVariable Long activityId) {
        managementService.removePTActivity(programId, activityId);
    }
}
