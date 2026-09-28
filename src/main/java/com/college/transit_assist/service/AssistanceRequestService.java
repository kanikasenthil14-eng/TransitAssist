package com.college.transit_assist.service;

import org.springframework.stereotype.Service;

import com.college.transit_assist.repository.AssistanceRequestRepository;
import com.college.transit_assist.repository.HelperRepository;
import com.college.transit_assist.entity.AssistanceRequest;
import com.college.transit_assist.entity.Helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AssistanceRequestService {

    private final AssistanceRequestRepository assistanceRequestRepository;
    private final HelperRepository helperRepository;

    public AssistanceRequestService(
            AssistanceRequestRepository assistanceRequestRepository,
            HelperRepository helperRepository) {

        this.assistanceRequestRepository = assistanceRequestRepository;
        this.helperRepository = helperRepository;
    }

    public AssistanceRequest saveRequest(AssistanceRequest request) {

        if (request.getStatus() == null) {
            request.setStatus("REQUESTED");
        }

        return assistanceRequestRepository.save(request);
    }

    public List<AssistanceRequest> getAllRequests() {
        return assistanceRequestRepository.findAll();
    }

    public AssistanceRequest getRequestById(Long id) {
        return assistanceRequestRepository.findById(id).orElse(null);
    }

    public AssistanceRequest assignHelper(Long requestId, Long helperId) {

        AssistanceRequest request =
                assistanceRequestRepository.findById(requestId).orElse(null);

        Helper helper =
                helperRepository.findById(helperId).orElse(null);

        // Helper or request does not exist, or helper is unavailable
        if (request == null || helper == null || !helper.isAvailable()) {
            return null;
        }

        // BUSINESS RULE 2:
        // Completed or cancelled requests cannot be reassigned
        if ("COMPLETED".equals(request.getStatus()) ||
            "CANCELLED".equals(request.getStatus())) {

            return null;
        }

        // BUSINESS RULE 1:
        // A helper cannot have overlapping assignments
        if (hasOverlappingAssignment(request, helper)) {
            return null;
        }

        request.setHelper(helper);
        request.setStatus("ASSIGNED");

        return assistanceRequestRepository.save(request);
    }

    public AssistanceRequest completeRequest(Long id) {

        AssistanceRequest request =
                assistanceRequestRepository.findById(id).orElse(null);

        if (request == null ||
            "COMPLETED".equals(request.getStatus()) ||
            "CANCELLED".equals(request.getStatus())) {

            return null;
        }

        request.setStatus("COMPLETED");

        return assistanceRequestRepository.save(request);
    }

    public AssistanceRequest cancelRequest(Long id) {

        AssistanceRequest request =
                assistanceRequestRepository.findById(id).orElse(null);

        if (request == null ||
            "COMPLETED".equals(request.getStatus()) ||
            "CANCELLED".equals(request.getStatus())) {

            return null;
        }

        request.setStatus("CANCELLED");

        return assistanceRequestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        assistanceRequestRepository.deleteById(id);
    }

    /**
     * BUSINESS RULE 1:
     *
     * Checks whether the selected helper already has
     * an active assignment that overlaps with the new request.
     *
     * Two assignments overlap when:
     *
     * existingStart < newEnd
     * AND
     * newStart < existingEnd
     *
     * Cancelled and completed requests are ignored.
     */
    private boolean hasOverlappingAssignment(
            AssistanceRequest request,
            Helper helper) {

        LocalDate date = request.getTripDate();
        LocalTime newStart = request.getPickupTime();
        LocalTime newEnd = request.getEndTime();

        // Invalid date/time information
        if (date == null ||
            newStart == null ||
            newEnd == null ||
            !newStart.isBefore(newEnd)) {

            return true;
        }

        // Get all existing requests
        List<AssistanceRequest> existingRequests =
                assistanceRequestRepository.findAll();

        for (AssistanceRequest existing : existingRequests) {

            // Ignore the same request
            if (existing.getRequestid().equals(request.getRequestid())) {
                continue;
            }

            // Check whether this request is assigned
            // to the same helper
            if (existing.getHelper() == null ||
                !existing.getHelper()
                        .getHelperId()
                        .equals(helper.getHelperId())) {

                continue;
            }

            // Completed and cancelled requests
            // should not block the helper
            if ("CANCELLED".equals(existing.getStatus()) ||
                "COMPLETED".equals(existing.getStatus())) {

                continue;
            }

            // Check whether the assignments are
            // on the same date
            if (!date.equals(existing.getTripDate()) ||
                existing.getPickupTime() == null ||
                existing.getEndTime() == null) {

                continue;
            }

            LocalTime existingStart = existing.getPickupTime();
            LocalTime existingEnd = existing.getEndTime();

            // Check for time overlap
            if (existingStart.isBefore(newEnd) &&
                newStart.isBefore(existingEnd)) {

                return true;
            }
        }

        return false;
    }
}