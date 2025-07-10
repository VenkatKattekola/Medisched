package org.example.medisched.dto;

import java.time.LocalDateTime; // Only need this one

public class AppointmentResponseDTO {
    private Long appointmentId;
    private LocalDateTime appointmentDate; // Changed to LocalDateTime
    private String status;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private String specialty;

    public AppointmentResponseDTO() {
    }

    // Updated Constructor
    public AppointmentResponseDTO(Long appointmentId, LocalDateTime appointmentDate, String status,
                                  Long patientId, Long doctorId, String doctorName, String specialty) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialty = specialty;
    }

    // Getters and Setters - updated for appointmentDate, removed startTime
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}