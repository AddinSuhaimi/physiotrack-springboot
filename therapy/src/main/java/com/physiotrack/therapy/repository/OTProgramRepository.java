package com.physiotrack.therapy.repository;

import com.physiotrack.therapy.model.OTProgram;

import org.springframework.stereotype.Repository;

@Repository
public interface OTProgramRepository
        extends TherapyProgramRepository<OTProgram> {
                OTProgram findByPatientId(Long patientId);
}
