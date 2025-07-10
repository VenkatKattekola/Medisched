package org.example.medisched.dto;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {
    private Long doctorid;
    private Long patientId;
    private LocalDateTime appointmentDate;
    private String status;

    // Constructors (optional, but good practice)
    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(Long doctorid, Long patientId, LocalDateTime appointmentDate, String status) {
        this.doctorid = doctorid;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // Getters and Setters (REQUIRED for Spring to map JSON)
    public Long getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(Long doctorid) {
        this.doctorid = doctorid;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}