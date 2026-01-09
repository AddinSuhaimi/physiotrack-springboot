package com.physiotrack.therapy.api;

import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.OTActivity;

import java.util.List;

public interface TherapyProgressService {

    // UC11 – View physiotherapy activities
    List<PTActivity> getPTActivities(Long programId);
    void markPTCompleted(Long programId, Long activityId);

    // UC12 – View occupational therapy activities
    List<OTActivity> getOTActivities(Long programId);
    void markOTCompleted(Long programId, Long activityId);
}
