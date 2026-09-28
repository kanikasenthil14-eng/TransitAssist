package com.college.transit_assist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name="helpers")
public class Helper {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long helperid;
    
    private String name;
    private String email;
    private String phone;
    private String assistanceType;
    private boolean available;

    public Long getHelperId() {
        return helperid;
    }
    public void setHelperId(Long helperid) {
        this.helperid = helperid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAssistanceType() {
        return assistanceType;
    }
    public void setAssistanceType(String assistanceType) {
        this.assistanceType = assistanceType;
    }

    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
