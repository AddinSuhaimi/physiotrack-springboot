package com.physiotrack.therapy.repository;

import com.physiotrack.therapy.model.PTProgram;
import org.springframework.stereotype.Repository;

@Repository
public interface PTProgramRepository
        extends TherapyProgramRepository<PTProgram> {
                PTProgram findByPatientId(Long patientId);
}

