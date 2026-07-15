package com.eman;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {
    List<Opportunity> findByGradeLevel(String gradeLevel);
    List<Opportunity> findByType(String type);
    List<Opportunity> findByGradeLevelAndType(String gradeLevel, String type);
}