package com.eman;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String deadline;
    private String eligibility;
    private Double minimumGpa;
    private String year;
    private String gradeLevel;  // NEW: e.g. "Middle School", "High School", "College Sophomore"
    private String link;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public Double getMinimumGpa() { return minimumGpa; }
    public void setMinimumGpa(Double minimumGpa) { this.minimumGpa = minimumGpa; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}