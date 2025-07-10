package org.example.medisched.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments") // Matches DB table name "appointments"
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="appointment_id") // Matches DB column name "appointment_id" (snake_case)
    private Long appointmentId; // Maps to BIGINT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PatientID") // Matches DB column name "PatientID" (PascalCase)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patients patient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DoctorID", nullable = false) // Matches DB column name "DoctorID" (PascalCase)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false) // Matches DB column name "appointment_date" (snake_case)
    private LocalDateTime appointmentDate; // Maps to datetime(6)

    @Enumerated(EnumType.STRING)
    @Column(name = "Status") // Matches DB column name "Status" (PascalCase)
    private Status status = Status.Scheduled; // Default set here

    @Column(name = "created_at", updatable = false, insertable = false) // Matches DB column name "created_at" (snake_case)
    private LocalDateTime createdAt = LocalDateTime.now(); // Maps to datetime(6)

    @Column(name = "updated_at", insertable = false) // Matches DB column name "updated_at" (snake_case)
    private LocalDateTime updatedAt; // Maps to datetime(6)

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- NEW FIELD: Link to the DoctorAvailability slot that this appointment booked ---
    // This column still needs to be added to your DB
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_slot_id", unique = true, nullable = true) // This will be a new column
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DoctorAvailability bookedAvailabilitySlot;
    // --- END NEW FIELD ---

    public enum Status {
        Scheduled, Completed, Cancelled, No_Show
    }

    // Getters and Setters (CORRECTED SETTERS BELOW)
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Patients getPatient() { return patient; }
    public void setPatient(Patients patient) { this.patient = patient; } // <-- CORRECTED

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; } // <-- CORRECTED

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; } // <-- CORRECTED

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; } // <-- CORRECTED

    // --- NEW GETTER AND SETTER for bookedAvailabilitySlot ---
    public DoctorAvailability getBookedAvailabilitySlot() {
        return bookedAvailabilitySlot;
    }

    public void setBookedAvailabilitySlot(DoctorAvailability bookedAvailabilitySlot) {
        this.bookedAvailabilitySlot = bookedAvailabilitySlot;
    }
    // --- END NEW GETTER AND SETTER ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointments that = (Appointments) o;
        // For JPA entities with generated IDs, it's common to check only the ID for equality
        // as transient entities (before saving) will have null IDs.
        return appointmentId != null && Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        // Use a constant or a calculation that's consistent for entities before they have an ID.
        // Once the ID is assigned, use the ID.
        // This is a common pattern for JPA entities.
        return appointmentId != null ? Objects.hash(appointmentId) : 31; // Using a prime number if ID is null
    }
}