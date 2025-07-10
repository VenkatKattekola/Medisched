package org.example.medisched.dto;

public class AppointmentStatusUpdateDTO {
    private String status; // Expects "Cancelled", "Completed", etc.

    public AppointmentStatusUpdateDTO() {}

    public AppointmentStatusUpdateDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}