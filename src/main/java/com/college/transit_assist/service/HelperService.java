package com.college.transit_assist.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.college.transit_assist.entity.AssistanceRequest;
import com.college.transit_assist.entity.Helper;
import com.college.transit_assist.repository.AssistanceRequestRepository;
import com.college.transit_assist.repository.HelperRepository;

@Service
public class HelperService {

    private final HelperRepository helperRepository;
    private final AssistanceRequestRepository assistanceRequestRepository;

    public HelperService(HelperRepository helperRepository,
                        AssistanceRequestRepository assistanceRequestRepository) {
        this.helperRepository = helperRepository;
        this.assistanceRequestRepository = assistanceRequestRepository;
    }

    public Helper saveHelper(Helper helper) {
        return helperRepository.save(helper);
    }

    public List<Helper> getAllHelpers() {
        return helperRepository.findAll();
    }

    public Helper getHelperById(Long id) {
        return helperRepository.findById(id).orElse(null);
    }

    public void deleteHelper(Long id) {
        helperRepository.deleteById(id);
    }

    // View helper workload for a particular day
    public List<AssistanceRequest> getHelperWorkload(Long helperId, LocalDate date) {

        List<AssistanceRequest> allRequests =
                assistanceRequestRepository.findAll();

        List<AssistanceRequest> workload =
                new java.util.ArrayList<>();

        for (AssistanceRequest request : allRequests) {

            if (request.getHelper() != null
                    && request.getHelper().getHelperId().equals(helperId)
                    && request.getTripDate().equals(date)) {

                workload.add(request);
            }
        }

        return workload;
    }
}