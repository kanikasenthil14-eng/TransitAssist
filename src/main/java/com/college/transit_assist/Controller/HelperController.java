package com.college.transit_assist.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import com.college.transit_assist.entity.AssistanceRequest;
import com.college.transit_assist.entity.Helper;
import com.college.transit_assist.service.HelperService;

@RestController
public class HelperController {

    private final HelperService helperService;

    public HelperController(HelperService helperService) {
        this.helperService = helperService;
    }

    @PostMapping("/helpers")
    public Helper saveHelper(@RequestBody Helper helper) {
        return helperService.saveHelper(helper);
    }

    @GetMapping("/helpers")
    public List<Helper> getAllHelpers() {
        return helperService.getAllHelpers();
    }

    @GetMapping("/helpers/{id}")
    public Helper getHelperById(@PathVariable Long id) {
        return helperService.getHelperById(id);
    }

    // Update helper
    @PutMapping("/helpers/{id}")
    public Helper updateHelper(@PathVariable Long id, @RequestBody Helper helper) {

        Helper existingHelper = helperService.getHelperById(id);

        if (existingHelper == null) {
            return null;
        }

        existingHelper.setName(helper.getName());
        existingHelper.setEmail(helper.getEmail());
        existingHelper.setPhone(helper.getPhone());
        existingHelper.setAssistanceType(helper.getAssistanceType());
        existingHelper.setAvailable(helper.isAvailable());

        return helperService.saveHelper(existingHelper);
    }

    // View helper workload for a particular day
    @GetMapping("/helpers/{helperId}/workload")
    public List<AssistanceRequest> getHelperWorkload(
            @PathVariable Long helperId,
            @RequestParam LocalDate date) {

        return helperService.getHelperWorkload(helperId, date);
    }

    @DeleteMapping("/helpers/{id}")
    public String deleteHelper(@PathVariable Long id) {
        helperService.deleteHelper(id);
        return "Helper deleted successfully";
    }
}