package com.jobautomation.app.dto;

import lombok.Data;

@Data
public class ApplicationStatusUpdate {
    private String status;  // APPLIED | RESPONSE | INTERVIEW | REJECTED | OFFER
    private String notes;
}
