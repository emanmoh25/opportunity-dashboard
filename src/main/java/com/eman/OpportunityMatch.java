package com.eman;

import java.util.List;

// Wraps an Opportunity with a match score (0-100) and the reasons behind that score.
public class OpportunityMatch {

    private Opportunity opportunity;
    private int score;
    private List<String> reasons;

    public OpportunityMatch(Opportunity opportunity, int score, List<String> reasons) {
        this.opportunity = opportunity;
        this.score = score;
        this.reasons = reasons;
    }

    public Opportunity getOpportunity() { return opportunity; }
    public void setOpportunity(Opportunity opportunity) { this.opportunity = opportunity; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
