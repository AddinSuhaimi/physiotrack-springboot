package com.physiotrack.test.repository;

import com.physiotrack.test.model.Test;
import com.physiotrack.test.model.TestType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    
    Optional<Test> findByType(TestType type);
}
