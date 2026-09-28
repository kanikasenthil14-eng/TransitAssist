package com.college.transit_assist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.time.LocalDate;

@Entity 
@Table(name="assistance_requests")
public class AssistanceRequest {

    @Id 
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long requestid;

    @ManyToOne 
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "helper_id")
    private Helper helper;

    private String pickupPoint;
    private String destination;
    private LocalDate tripDate;
    private LocalTime pickupTime;
    private LocalTime endTime;
    private String assistanceType;
    private String status;
    
    public Long getRequestid() {
        return requestid;
    }
    public void setRequestid(Long requestid) {
        this.requestid = requestid;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Helper getHelper() {
        return helper;
    }
    public void setHelper(Helper helper) {
        this.helper = helper;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }
    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }
    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getAssistanceType() {
        return assistanceType;
    }
    public void setAssistanceType(String assistanceType) {
        this.assistanceType = assistanceType;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
