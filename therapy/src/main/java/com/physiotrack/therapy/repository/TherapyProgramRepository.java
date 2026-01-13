package com.physiotrack.therapy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TherapyProgramRepository<T>
        extends JpaRepository<T, Long> {
}
