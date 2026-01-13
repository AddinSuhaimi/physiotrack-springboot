package com.physiotrack.therapy.api;

import com.physiotrack.therapy.model.PTActivity;
import com.physiotrack.therapy.model.OTActivity;

public interface TherapyManagementService {

    // UC20 – Modify physiotherapy program
    void addPTActivity(Long programId, PTActivity activity);
    void removePTActivity(Long programId, Long activityId);

    // UC21 – Modify occupational therapy program
    void addOTActivity(Long programId, OTActivity activity);
    void removeOTActivity(Long programId, Long activityId);
}
