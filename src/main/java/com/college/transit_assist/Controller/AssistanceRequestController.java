package com.college.transit_assist.Controller;

import com.college.transit_assist.entity.AssistanceRequest;
import com.college.transit_assist.service.AssistanceRequestService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class AssistanceRequestController {

    private final AssistanceRequestService assistanceRequestService;

    public AssistanceRequestController(AssistanceRequestService assistanceRequestService) {
        this.assistanceRequestService = assistanceRequestService;
    }

    // Create assistance request
    @PostMapping("/requests")
    // @RequestBody converts the incoming JSON into an AssistanceRequest object.
    public AssistanceRequest saveRequest(@RequestBody AssistanceRequest request) {
        return assistanceRequestService.saveRequest(request);
    }

    // Get all assistance requests
    @GetMapping("/requests")
    public List<AssistanceRequest> getAllRequests() {
        return assistanceRequestService.getAllRequests();
    }

    // Get assistance request by ID
    @GetMapping("/requests/{id}")
    public AssistanceRequest getRequestById(@PathVariable Long id) {
        return assistanceRequestService.getRequestById(id);
    }

    // Complete assistance request
    @PutMapping("/requests/{id}/complete")
    public ResponseEntity<AssistanceRequest> completeRequest(@PathVariable Long id) {
        AssistanceRequest request = assistanceRequestService.completeRequest(id);
        return request == null
                ? ResponseEntity.status(HttpStatus.CONFLICT).build()
                : ResponseEntity.ok(request);
    }

    // Cancel assistance request
    @PutMapping("/requests/{id}/cancel")
    public ResponseEntity<AssistanceRequest> cancelRequest(@PathVariable Long id) {
        AssistanceRequest request = assistanceRequestService.cancelRequest(id);
        return request == null
                ? ResponseEntity.status(HttpStatus.CONFLICT).build()
                : ResponseEntity.ok(request);
    }

    // Assign helper to assistance request
    @PutMapping("/requests/{requestId}/assign/{helperId}")
        public ResponseEntity<AssistanceRequest> assignHelper(
            @PathVariable Long requestId,
            @PathVariable Long helperId) {

        AssistanceRequest request = assistanceRequestService.assignHelper(
                requestId,
                helperId
        );
        return request == null
            ? ResponseEntity.status(HttpStatus.CONFLICT).build()
            : ResponseEntity.ok(request);
    }

    // Delete assistance request
    @DeleteMapping("/requests/{id}")
    public String deleteRequest(@PathVariable Long id) {
        assistanceRequestService.deleteRequest(id);
        return "Assistance request deleted successfully";
    }
}