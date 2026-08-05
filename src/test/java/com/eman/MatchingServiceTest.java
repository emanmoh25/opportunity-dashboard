package com.eman;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private OpportunityRepository opportunityRepository;

    @InjectMocks
    private MatchingService matchingService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setName("Test Student");
        student.setGpa(3.2);
        student.setGradeLevel("College Sophomore");
        student.setInterests(List.of("Fellowship"));
    }

    private Opportunity buildOpportunity(String type, Double minimumGpa, String gradeLevel) {
        Opportunity opportunity = new Opportunity();
        opportunity.setName("Test Opportunity");
        opportunity.setType(type);
        opportunity.setMinimumGpa(minimumGpa);
        opportunity.setGradeLevel(gradeLevel);
        return opportunity;
    }

    @Test
    void perfectMatchScoresMaximumPoints() {
        Opportunity opportunity = buildOpportunity("Fellowship", 3.0, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        assertEquals(1, matches.size());
        assertEquals(100, matches.get(0).getScore());
    }

    @Test
    void gpaBelowRequirementByLessThanMarginGetsPartialCredit() {
        // Student GPA is 3.2, requirement is 3.4, gap of 0.2 is within the 0.3 margin
        Opportunity opportunity = buildOpportunity("Fellowship", 3.4, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        // 20 (partial GPA) + 35 (grade level) + 30 (interest) = 85
        assertEquals(85, matches.get(0).getScore());
    }

    @Test
    void gpaFarBelowRequirementStillReturnsLowNonZeroScore() {
        // Student GPA is 3.2, requirement is 3.9, gap of 0.7 is beyond the margin
        Opportunity opportunity = buildOpportunity("Fellowship", 3.9, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        // 5 (low GPA credit) + 35 (grade level) + 30 (interest) = 70
        // The opportunity should never be filtered out entirely, since GPA is holistic.
        assertEquals(70, matches.get(0).getScore());
        assertTrue(matches.get(0).getScore() > 0);
    }

    @Test
    void noGpaRequirementAlwaysGetsFullGpaCredit() {
        Opportunity opportunity = buildOpportunity("Fellowship", null, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        assertEquals(100, matches.get(0).getScore());
    }

    @Test
    void gradeLevelMismatchGetsLowButNonZeroCredit() {
        Opportunity opportunity = buildOpportunity("Fellowship", 3.0, "High School");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        // 35 (GPA) + 5 (grade level mismatch) + 30 (interest) = 70
        assertEquals(70, matches.get(0).getScore());
    }

    @Test
    void interestMismatchGetsZeroInterestCredit() {
        Opportunity opportunity = buildOpportunity("Scholarship", 3.0, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        // 35 (GPA) + 35 (grade level) + 0 (interest mismatch) = 70
        assertEquals(70, matches.get(0).getScore());
    }

    @Test
    void studentWithNoInterestsGetsNeutralCredit() {
        student.setInterests(List.of());
        Opportunity opportunity = buildOpportunity("Scholarship", 3.0, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        // 35 (GPA) + 35 (grade level) + 15 (neutral interest) = 85
        assertEquals(85, matches.get(0).getScore());
    }

    @Test
    void resultsAreSortedHighestScoreFirst() {
        Opportunity lowMatch = buildOpportunity("Scholarship", 3.9, "High School");
        Opportunity highMatch = buildOpportunity("Fellowship", 3.0, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(lowMatch, highMatch));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        assertEquals(2, matches.size());
        assertTrue(matches.get(0).getScore() >= matches.get(1).getScore());
        assertEquals(highMatch, matches.get(0).getOpportunity());
    }

    @Test
    void everyMatchIncludesReasons() {
        Opportunity opportunity = buildOpportunity("Fellowship", 3.0, "College Sophomore");
        when(opportunityRepository.findAll()).thenReturn(List.of(opportunity));

        List<OpportunityMatch> matches = matchingService.findMatchesForStudent(student);

        assertFalse(matches.get(0).getReasons().isEmpty());
    }
}
