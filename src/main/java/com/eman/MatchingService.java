package com.eman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchingService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    // GPA requirements are often more of a guideline than a hard cutoff, so
    // being close to the minimum still earns partial credit instead of zero.
    private static final double GPA_CLOSE_ENOUGH_MARGIN = 0.3;

    private static final int GPA_WEIGHT = 35;
    private static final int GPA_PARTIAL_CREDIT = 20;
    private static final int GPA_LOW_CREDIT = 5;

    private static final int GRADE_LEVEL_WEIGHT = 35;
    private static final int GRADE_LEVEL_MISMATCH_CREDIT = 5;

    private static final int INTEREST_WEIGHT = 30;
    private static final int INTEREST_NEUTRAL_CREDIT = 15;

    public List<OpportunityMatch> findMatchesForStudent(Student student) {
        List<Opportunity> allOpportunities = opportunityRepository.findAll();
        List<OpportunityMatch> matches = new ArrayList<>();

        for (Opportunity opportunity : allOpportunities) {
            matches.add(scoreOpportunity(student, opportunity));
        }

        matches.sort(Comparator.comparingInt(OpportunityMatch::getScore).reversed());
        return matches;
    }

    private OpportunityMatch scoreOpportunity(Student student, Opportunity opportunity) {
        List<String> reasons = new ArrayList<>();
        int score = 0;

        score += scoreGpa(student, opportunity, reasons);
        score += scoreGradeLevel(student, opportunity, reasons);
        score += scoreInterest(student, opportunity, reasons);

        return new OpportunityMatch(opportunity, score, reasons);
    }

    private int scoreGpa(Student student, Opportunity opportunity, List<String> reasons) {
        Double minimumGpa = opportunity.getMinimumGpa();

        if (minimumGpa == null) {
            reasons.add("No GPA requirement listed");
            return GPA_WEIGHT;
        }

        if (student.getGpa() == null) {
            reasons.add("GPA not provided, assumed neutral fit");
            return GPA_PARTIAL_CREDIT;
        }

        if (student.getGpa() >= minimumGpa) {
            reasons.add("GPA requirement met");
            return GPA_WEIGHT;
        }

        double gap = minimumGpa - student.getGpa();
        if (gap <= GPA_CLOSE_ENOUGH_MARGIN) {
            reasons.add("GPA is close to the requirement, many programs review holistically");
            return GPA_PARTIAL_CREDIT;
        }

        reasons.add("GPA is below the stated requirement");
        return GPA_LOW_CREDIT;
    }

    private int scoreGradeLevel(Student student, Opportunity opportunity, List<String> reasons) {
        String requiredLevel = opportunity.getGradeLevel();

        if (requiredLevel == null || requiredLevel.isBlank()) {
            reasons.add("Open to all grade levels");
            return GRADE_LEVEL_WEIGHT;
        }

        if (student.getGradeLevel() != null && student.getGradeLevel().equalsIgnoreCase(requiredLevel)) {
            reasons.add("Grade level matches");
            return GRADE_LEVEL_WEIGHT;
        }

        reasons.add("Grade level does not match, though some programs are flexible");
        return GRADE_LEVEL_MISMATCH_CREDIT;
    }

    private int scoreInterest(Student student, Opportunity opportunity, List<String> reasons) {
        List<String> interests = student.getInterests();
        String type = opportunity.getType();

        if (interests == null || interests.isEmpty()) {
            reasons.add("No interests specified");
            return INTEREST_NEUTRAL_CREDIT;
        }

        if (type == null || type.isBlank()) {
            reasons.add("Opportunity type not specified");
            return INTEREST_NEUTRAL_CREDIT;
        }

        for (String interest : interests) {
            if (interest.equalsIgnoreCase(type)) {
                reasons.add("Matches your interest in " + type);
                return INTEREST_WEIGHT;
            }
        }

        reasons.add("Doesn't match your stated interests");
        return 0;
    }
}
