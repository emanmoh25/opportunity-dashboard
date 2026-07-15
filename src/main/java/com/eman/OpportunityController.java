package com.eman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @GetMapping
    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    @PostMapping
    public Opportunity createOpportunity(@RequestBody Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    @GetMapping("/{id}")
    public Opportunity getOpportunityById(@PathVariable Long id) {
        return opportunityRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Opportunity updateOpportunity(@PathVariable Long id, @RequestBody Opportunity updatedOpportunity) {
        return opportunityRepository.findById(id).map(opportunity -> {
            opportunity.setName(updatedOpportunity.getName());
            opportunity.setType(updatedOpportunity.getType());
            opportunity.setDeadline(updatedOpportunity.getDeadline());
            opportunity.setEligibility(updatedOpportunity.getEligibility());
            opportunity.setMinimumGpa(updatedOpportunity.getMinimumGpa());
            opportunity.setYear(updatedOpportunity.getYear());
            opportunity.setLink(updatedOpportunity.getLink());
            return opportunityRepository.save(opportunity);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteOpportunity(@PathVariable Long id) {
        opportunityRepository.deleteById(id);
        return "Deleted opportunity with id " + id;
    }
    @GetMapping("/filter")
    public List<Opportunity> filterOpportunities(
            @RequestParam(required = false) String gradeLevel,
            @RequestParam(required = false) String type) {

        if (gradeLevel != null && type != null) {
            return opportunityRepository.findByGradeLevelAndType(gradeLevel, type);
        } else if (gradeLevel != null) {
            return opportunityRepository.findByGradeLevel(gradeLevel);
        } else if (type != null) {
            return opportunityRepository.findByType(type);
        } else {
            return opportunityRepository.findAll();
        }
    }
}