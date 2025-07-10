package org.example.medisched.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime; // Not directly used but good to keep if other time types are needed
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Doctor_Availability") // Using the exact name you provided
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long availabilityId; // <-- This is the ID for DoctorAvailability

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DoctorID", nullable = false, columnDefinition = "BIGINT UNSIGNED") // Using the exact name you provided
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @Column(nullable = false, columnDefinition = "Date")
    private LocalDate availableDate;

    @Column(nullable = false, columnDefinition = "Time")
    private LocalTime startTime;

    @Column(nullable = false, columnDefinition = "Time")
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean isBooked = false;

    // --- FIX: Add @JsonIgnore here ---
    @OneToOne(mappedBy = "bookedAvailabilitySlot", fetch = FetchType.LAZY, optional = true)
    @JsonIgnore // <--- ADD THIS LINE
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Keep this if you want
    private Appointments appointment;
    // --- END FIX ---

    public Long getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isBooked() { // Standard getter for boolean fields
        return isBooked;
    }

    public void setIsBooked(boolean booked) { // Standard setter for boolean fields
        isBooked = booked;
    }

    // --- NEW GETTER AND SETTER for appointment ---
    public Appointments getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointments appointment) {
        this.appointment = appointment;
    }
    // --- END NEW GETTER AND SETTER ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorAvailability that = (DoctorAvailability) o; // <-- CORRECTED CAST
        // Compare using the availabilityId, which is the primary key of DoctorAvailability
        return availabilityId != null && Objects.equals(availabilityId, that.availabilityId);
    }

    @Override
    public int hashCode() {
        // For JPA entities with generated IDs, using getClass().hashCode() is safer
        // until the entity is persisted and has a non-null ID.
        return getClass().hashCode();
        // If availabilityId is guaranteed to be non-null when hashCode is called (e.g., only on loaded entities)
        // you could use Objects.hash(availabilityId);
    }
}