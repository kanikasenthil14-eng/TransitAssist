package com.college.transit_assist.repository;

import com.college.transit_assist.entity.AssistanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistanceRequestRepository extends JpaRepository<AssistanceRequest, Long> {
}