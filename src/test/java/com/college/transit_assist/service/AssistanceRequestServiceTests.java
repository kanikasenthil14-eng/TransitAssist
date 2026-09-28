package com.college.transit_assist.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.college.transit_assist.entity.AssistanceRequest;
import com.college.transit_assist.repository.AssistanceRequestRepository;
import com.college.transit_assist.repository.HelperRepository;

class AssistanceRequestServiceTests {

    private final AssistanceRequestRepository requestRepository = mock(AssistanceRequestRepository.class);
    private final HelperRepository helperRepository = mock(HelperRepository.class);
    private final AssistanceRequestService service =
            new AssistanceRequestService(requestRepository, helperRepository);

    @Test
    void completedRequestsCannotChangeStatus() {
        assertTerminalStatusCannotChange("COMPLETED");
    }

    @Test
    void cancelledRequestsCannotChangeStatus() {
        assertTerminalStatusCannotChange("CANCELLED");
    }

    private void assertTerminalStatusCannotChange(String status) {
        AssistanceRequest request = new AssistanceRequest();
        request.setStatus(status);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));

        assertNull(service.completeRequest(1L));
        assertNull(service.cancelRequest(1L));
        verify(requestRepository, never()).save(any(AssistanceRequest.class));
    }
}